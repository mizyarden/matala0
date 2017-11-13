

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.ReadOnlyFileSystemException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

//import org.junit.Assert;

//import de.micromata.jak.examples.Double;
//import de.micromata.jak.examples.HashMap;
//import de.micromata.jak.examples.String;
//import de.micromata.jak.examples.Utils;
//import de.micromata.opengis.kml.v_2_2_0.Document;
//import de.micromata.opengis.kml.v_2_2_0.Folder;
//import de.micromata.opengis.kml.v_2_2_0.Kml;
//import de.micromata.opengis.kml.v_2_2_0.Placemark;

//kml functions from https://mvnrepository.com/artifact/de.micromata.jak/JavaAPIforKml/2.2.0
//                   https://github.com/micromata/javaapiforkml


public class Function{

	public static ArrayList<String> getAllcsvFileListFromFolder(String directoryName){

		ArrayList<String> fileList = new ArrayList<String>();
		File directory = new File(directoryName);

		//get all the files from a directory

		if (!directory.isDirectory()) {
			return fileList;
		}
		File[] fList = directory.listFiles();

		for (File file : fList){

			if (file.isFile()){

				if(file.getAbsolutePath().endsWith(".csv")){
					fileList.add(file.getAbsolutePath());
				}

			} else if (file.isDirectory()){

				fileList.addAll(getAllcsvFileListFromFolder(file.getAbsolutePath()));

			}

		}
		return fileList;

	}
	public static void readFileAndAddToList(String fileName, LinkedList<WiFiLinkedList> wifiList ) {

		try {
			FileReader Fr = new FileReader(fileName);
			BufferedReader BR= new BufferedReader(Fr); 
			int count=0;
			WiFiLinkedList wll = new WiFiLinkedList();
			String Line = BR.readLine();
			String[] firstLine = Line.split(",");
			String UID = firstLine[5].substring(8);
			while(Line != null){
				if(count==0){
					if(!Line.contains("WigleWifi")){
						System.out.println("Error: File must be from WigleWifi.");
						break;
					}
				}
				count++;
				if(count>2){
					String[] arr = Line.split(",");

					//Creating WIFILIST
					String time = arr[3];
					double alt = Double.parseDouble(arr[8]);
					double lat =  Double.parseDouble(arr[6]);
					double lon = Double.parseDouble(arr[7]);
					if (wifiList.size() == 0){
						wll = new WiFiLinkedList(lat, lon, alt, time, UID);
						wifiList.add(wll);
					}

					//System.out.println(Arrays.toString(arr));

					//Creating WiFi
					String SSID = arr[1],MAC = arr[0];
					double freq = Double.parseDouble(arr[4]) , signal = Double.parseDouble(arr[5]);
					WiFi wf = new WiFi(SSID,MAC ,freq ,signal);

					if(!wll.IsBelong(lat, lon, time)){
						wll = new WiFiLinkedList(lat, lon, alt, time, UID);
						wifiList.add(wll);		//adding WiFiLinkedList to LinkedList (ans)
					}
					wll.add(wf);		//adding WiFi to WiFiLinkedList
				}
				Line = BR.readLine();		//התקדמות
			}
			BR.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void filtercsvFileByTime(String csvFile, String pathToWriteKML, String startTime, String endTime ){
		try {
			int lineYear, lineMonth, lineDay, lineHours, lineMins;
			int startYear, startMonth, startDay, startHours, startMins;
			int endYear, endMonth, endDay, endHours, endMins;
			startYear = Integer.parseInt(startTime.substring(0, 4));
			startMonth = Integer.parseInt(startTime.substring(5, 7));
			startDay = Integer.parseInt(startTime.substring(8, 10));
			endYear = Integer.parseInt(endTime.substring(0, 4));
			endMonth = Integer.parseInt(endTime.substring(5, 7));
			endDay = Integer.parseInt(endTime.substring(8, 10));
			
			FileReader fr = new FileReader(csvFile);
			BufferedReader br= new BufferedReader(fr); 
			ArrayList<String> filteredCSV = new ArrayList<String>();
			String brFirstLine = br.readLine();
			String brLine = br.readLine();
			String[] line = brLine.split(",");
			String timeColumn = line[0];
			String hmsStart = startTime.substring(11, timeColumn.length()-1); //hms = hour, minutes, seconds
			String hmsEnd = endTime.substring(11, timeColumn.length()-1);
			String lineDate = "";
			
			LocalTime lineTime = null;
			while(brLine != null){
				lineTime = LocalTime.parse(brLine.substring(11, timeColumn.length()-1));
				lineDate = brLine.substring(0, 10);
				lineYear = Integer.parseInt(brLine.substring(0, 4));
				lineMonth = Integer.parseInt(brLine.substring(5, 7));
				lineDay = Integer.parseInt(brLine.substring(8, 10));
				if(lineTime.isAfter(LocalTime.parse(hmsStart)) && lineTime.isBefore(LocalTime.parse(hmsEnd)) && lineYear>=startYear 
						&& lineYear<=endYear && lineMonth>=startMonth && lineMonth<=endMonth && lineDay>=startDay && lineDay<=endDay){
					filteredCSV.add(brLine);
					brLine = br.readLine();
				}
				else brLine = br.readLine();
			}
			fr.close();
			br.close();
			
			ArrayList<String[]> filtered2 = new ArrayList<String[]>();
			for(int i = 0; i < filteredCSV.size(); i++)
			{
				filtered2.add(filteredCSV.get(i).split(","));
			}
			
			
			
			
//			Kml kml = new Kml();
//			Document doc = kml.createAndSetDocument().withName("file").withOpen(true);
//			Folder folder = doc.createAndAddFolder();
//			folder.withName("folder").withOpen(true);
//			for(String row : filteredCSV)
//			{
//				String[] rowArr = row.split(" ,");
//				mac = rowArr[5].substring(8, 24);
//				lat = Double.parseDouble(rowArr[2]);
//				lon = Double.parseDouble(rowArr[3]);
//				alt = Double.parseDouble(rowArr[4]);
//				createPlacemarkWithChart(doc, folder, lon, lat, alt, mac);
//			}
//			
//			kml.marshal(new File("C:\\Users\\Dan\\Desktop\\csvToKml.kml"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {

		LinkedList<WiFiLinkedList> wifiList = new LinkedList<WiFiLinkedList>();
		String folder = "C:\\Users\\Yarden\\Desktop\\New Folder (2)";

		ArrayList<String> fileList = getAllcsvFileListFromFolder(folder);
		for (String csvFileName : fileList) {
			readFileAndAddToList(csvFileName, wifiList);
		}
		//
		for (int i = 0; i < wifiList.size(); i++) {
			WiFi[] result = Best10WIFI(wifiList.get(i).getArrWiFi());
			wifiList.get(i).setWiFiList(result);    //רשימה איי
		}
		for (int i = 0; i < wifiList.size(); i++) {
			System.out.println(wifiList.get(i));///

		}
		//הדפסה לתוך csv 
		String CSVFile="C:/Users/Yarden/Desktop/New folder//new.csv";
		PrintWriter pw = null;
		try {
			pw  = new PrintWriter(new File(CSVFile));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
		StringBuilder builder = new StringBuilder();
		String ColumnNamesList = "TIME,ID,LAT,LON,ALT,WIFI NETWORK,SIGNAL1,MAC1,SSID1,FREQNCY1,SIGNAL2,MAC2,SSID2,FREQNCY2,SIGNAL3,MAC3,SSID3,FREQNCY3,SIGNAL4,MAC4,SSID4,FREQNCY4,SIGNAL5,MAC5,SSID5,FREQNCY5,SIGNAL6,MAC6,SSID6,FREQNCY6,SIGNAL7,MAC7,SSID7,FREQNCY7,SIGNAL8,MAC8,SSID8,FREQNCY8,SIGNAL9,MAC9,SSID9,FREQNCY9,SIGNAL10,MAC10,SSID10,FREQNCY10";
		for (int i = 0; i < wifiList.size(); i++) {
			if (i==0){
				builder.append(ColumnNamesList +"\n");}
			else{ builder.append(wifiList.get(i));
			builder.append('\n');}
			if(i+1==wifiList.size()){
				pw.write(builder.toString());
				pw.close();
			}
		}
		String kmlFolder="C:\\Users\\Yarden\\Desktop\\New folder\\BookTime.kml";
		filtercsvFileByTime(CSVFile, kmlFolder,  "2017:11:06:09:40:00","2017:11:06:10:00:00");
		
		//kml
		
		

		System.out.println("done!");
//		try{
//		FileReader fr = new FileReader(CSVFile);
//		BufferedReader br= new BufferedReader(fr);
//		String line = br.readLine();
//		ArrayList<String> csvFile = new ArrayList<String>();
//		for (int i = 0; i < args.length; i++) {
//		
//			csvFile.add(line);
//			line=br.readLine();
//		}
//		Kml kml = new Kml(csvFile);
//		kml.convertCSVToKML(csvFile);
//		kml.makeKmlFile(csvFile);
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//		}
				
	}
//	private static void createPlacemarkWithChart(Document document, Folder folder, double longitude, double latitude, double altitude, 
//		    String mac) {
//		Placemark placemark = folder.createAndAddPlacemark();
//		// use the style for each continent
//		placemark.withName(mac)
//		    // coordinates and distance (zoom level) of the viewer
//		    .createAndSetLookAt().withLongitude(longitude).withLatitude(latitude).withAltitude(altitude).withRange(12000000);
//		
//		
//		placemark.createAndSetPoint().addToCoordinates(longitude, latitude); // set coordinates
//		
//	}
	private static void swap(WiFi[] arr, int i, int j){
		WiFi temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	private static WiFi[] Best10WIFI(WiFi[] ans) {
		boolean flag= true;
		for (int i = 0; i < ans.length && flag; i++) {
			flag=false;
			for (int j = 0; j < ans.length-1; j++) {
				if(ans[j].getSignal()<ans[j+1].getSignal() ){
					flag=true;
					swap(ans,j,j+1);
				}
			}
		}
		WiFi [] first10= new WiFi[Math.min(10,ans.length)];
		for (int i = 0; i < first10.length; i++) {
			first10[i]=ans[i];
		}
		return first10;
	}
	
	
}

