import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Main {

	public static void main(String[] args) {
		String sURL = "http://bimobject.com/sv/product/getproducts/";
		String fileName = "BIM.dat";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			System.out.println("Downloading BIMobject.com data...");
			URL url = new URL(sURL);
			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JSONParser jp = new JSONParser();
			JSONObject obj = (JSONObject) jp.parse(new InputStreamReader((InputStream) request.getContent()));
			JSONObject data = (JSONObject) obj.get("Data");
			int downloads = Integer.parseInt(data.get("NumberOfDownloads").toString());
			int products = Integer.parseInt(data.get("NumberOfProducts").toString());
			int manufacturers = Integer.parseInt(data.get("NumberOfManufacturers").toString());
			System.out.println("Download succesful.");
			System.out.println("Number of downloads: " + downloads);
			System.out.println("Number of products: " + products);
			System.out.println("Number of manufacturers: " + manufacturers);
			
			Calendar cal = Calendar.getInstance();
			
			StringBuilder sb = new StringBuilder("\n")
					.append(df.format(cal.getTime()))
					.append("\t");
					
			Scanner sc = new Scanner(new File(fileName));
			sc.useDelimiter("\t");
			String tempProd = "0";
			String tempProd2 = "0";
			String tempManufacturers = "0";
			String tempManufacturers2 = "0";
			int lastNumOfProducts;
			int lastNumOfManufacturers;
			
			while(sc.hasNextLine()) {
				sc.next();
				tempProd = sc.next();
				if (!tempProd.isEmpty()) tempProd2 = tempProd;
				sc.next();
				tempManufacturers = sc.next();
				if (!tempManufacturers.isEmpty()) tempManufacturers2 = tempManufacturers;
				sc.nextLine();
			}
			
			sc.close();	
			
			lastNumOfProducts = Integer.parseInt(tempProd2);
			lastNumOfManufacturers = Integer.parseInt(tempManufacturers2);
			
			if (lastNumOfProducts != products) sb.append(products);
			
			sb.append("\t").append(downloads).append("\t");
			
			if (lastNumOfManufacturers != manufacturers) sb.append(manufacturers);
			
			sb.append("\t");
			
			try {
				Files.write(Paths.get(fileName), sb.toString().getBytes(), StandardOpenOption.APPEND);
			} catch (Exception e) {
			    e.printStackTrace();
			}


		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
