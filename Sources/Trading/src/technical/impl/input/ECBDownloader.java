package technical.impl.input;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import business.api.CurrencyType;


public class ECBDownloader {
	
	
	public static void main(String[] args) {
		try {
			
			File downloadDir = new File("./download/");
			String[] urls_for_download = ECBLinks.datalinks;
			Proxy proxy = getProxy(args);
			
			download(downloadDir, urls_for_download, proxy);
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	private static void download(File downloadDir, String[] urls, Proxy proxy) throws MalformedURLException, IOException, ParserConfigurationException, SAXException, ParseException {
		long start_total = System.currentTimeMillis();
		for (int i=0; i<urls.length; i++) {
			long start = System.currentTimeMillis();
			System.out.print("Downloading " + urls[i] + " ... ");
			PairData data = getPairDataByURL(urls[i], proxy);
			File file = savePairData(downloadDir, data);
			long end = System.currentTimeMillis();
			System.out.println("OK " + (end - start) + "ms	->  " /*+ CurrencyType.toString(data.getFromType())
								+ "_" + CurrencyType.toString(data.getToType()) + "	"*/ + file.getAbsolutePath());
		}
		long end_total = System.currentTimeMillis();
		System.out.println("Time " + (end_total - start_total) + "ms");
	}


	private static File savePairData(File downloadDir, PairData data) throws IOException {
		File dir = new File(downloadDir, data.getId());
		dir.mkdirs();
		
		File file = new File(dir, CurrencyType.toString(data.getFromType()) + "2" + CurrencyType.toString(data.getToType()) + ".csv");
		OutputStream os = null;
		PrintWriter pw = null;
		
		try {
			os = new FileOutputStream(file);
			pw = new PrintWriter(os);
			
			List<Timestamp> times = data.getTimes();
			List<Double> rates = data.getRates();
			for (int i=0; i<times.size(); i++) {
				Timestamp time = times.get(i);
				Double rate = rates.get(i);
				DateFormat format = new SimpleDateFormat("yyyy/MM/dd");
				String line = format.format(time) + ", " + rate + "\r\n";
				//System.out.println(line);
				pw.write(line);
			}
		} finally {
			if (pw != null) try {pw.close();} catch (Exception e) {}
			if (os != null) try {os.close();} catch (Exception e) {}
		}
		
		return file;
	}
	
	
	private static PairData getPairDataByURL(String url, Proxy proxy) throws MalformedURLException, IOException, ParserConfigurationException, SAXException, ParseException {
		Document doc = getDocumentByURL(url, proxy);
		PairData data = new PairData();
		traverse(doc.getFirstChild(), data);
		validate(data);
		return data;
	}
	
	
	private static void validate(PairData data) {
		if(data.getId() == null) {
			throw new IllegalStateException("ID is null");
		}
		if(data.getFromType() == -1) {
			throw new IllegalStateException("'From' type is -1");
		}
		if(data.getToType() == -1) {
			throw new IllegalStateException("'To' type is -1");
		}
		if (data.getTimes().size() < 100) {
			throw new IllegalStateException("Rates size is less than 100: " + data.getTimes().size());
		}
	}
	
	
	private static void traverse(Node node, PairData data) throws ParseException {
		NodeList childNodes = node.getChildNodes();
		for (int i=0; i<childNodes.getLength(); i++) {
			Node item = childNodes.item(i);
			if (item instanceof Element) {
				
				Element element = (Element) item;
				
				if (item.getNodeName().equals("ID")) {
					String id = element.getTextContent();
					
					data.setID(id);	
				}
				
				if (item.getNodeName().equals("Group")) {
					String currency = element.getAttribute("CURRENCY");
					String base_currency = element.getAttribute("CURRENCY_DENOM");
					
					data.setCurrencyType(CurrencyType.toID(base_currency), CurrencyType.toID(currency));	
				}
				
				if (item.getNodeName().equals("Obs")) {
					String time = element.getAttribute("TIME_PERIOD");
					String value = element.getAttribute("OBS_VALUE");
					  
					DateFormat formatter = new SimpleDateFormat("yy-MM-dd");
					Date date = formatter.parse(time); 
					Timestamp timestamp = new Timestamp(date.getTime());
					
					data.add(timestamp, Double.parseDouble(value));
				}
				
				traverse(item, data);
			}
		}
	}
	
	
	private static Document getDocumentByURL(String url, Proxy proxy) throws MalformedURLException, IOException, ParserConfigurationException, SAXException {
		InputStream xmlDataIS = getInputStreamByURL(url, proxy);
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document doc = builder.parse(xmlDataIS);
		return doc;
	}
	
	
	private static InputStream getInputStreamByURL(String url, Proxy proxy) throws MalformedURLException, IOException {
		URL eurgbp_url = new URL(url);
		URLConnection connection = (proxy == null) ? eurgbp_url.openConnection() : eurgbp_url.openConnection(proxy);
		InputStream is = connection.getInputStream();
		return new BufferedInputStream(is, 2048);
	}
	
	
	private static Proxy getProxy(String[] args) {
		//Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy", 8083)); //get the proxies dynamically
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.wdf.sap.corp", 8080));
		//proxy.wdf.sap.corp:8080
		return proxy;
	}
	
	
	private static class PairData {
		
		
		private String id;
		private int fromType;
		private int toType;
		private List<Timestamp> times;
		private List<Double> rates;
		
		
		PairData() {
			times = new ArrayList<Timestamp>();
			rates = new ArrayList<Double>();
		}
		
		void setID(String _id) {
			id = _id;
		}
		
		void setCurrencyType(int _fromType, int _toType) {
			fromType = _fromType;
			toType = _toType;
		}
		
		void add(Timestamp timestamp, Double rate) {
			times.add(timestamp);
			rates.add(rate);
		}
		
		public String getId() {
			return id;
		}

		public int getFromType() {
			return fromType;
		}

		public int getToType() {
			return toType;
		}

		public List<Timestamp> getTimes() {
			return times;
		}

		public List<Double> getRates() {
			return rates;
		}
	}
}
