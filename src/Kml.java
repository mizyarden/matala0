import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Kml {

	private ArrayList<String> kmlFile;
	private int size;
	

	public Kml(ArrayList<String> csvFile) {
		size = csvFile.size();
	}

	public void convertCSVToKML(ArrayList<String> csvfile)
	{
		String[] mac = new String[size];
		double[] lat = new double[size];
		double[] alt = new double[size];
		double[] lon = new double[size];
		String[] id = new String[size];
		String[] time = new String[size];

		String[] line;
		for(int i = 0; i < size ; i++){
			line = csvfile.get(i).split(" ,");
			mac[i] = line[7];
			lat[i] = Double.parseDouble(line[2]);
			lon[i] = Double.parseDouble(line[3]);
			alt[i] = Double.parseDouble(line[4]);
			id[i] = line[1];
			time[i]=line[0];
		kmlScanCoordinateGenerator(lon[i], lat[i], alt[i], id[i], time[i]);
		}
		
	}

	public String kmlScanCoordinateGenerator(double lon,double lat,double alt,String pointName,String Desc){// adds one point to the kml - used for the scans
		String all = "<Placemark>\n           <name>"+pointName+"</name>\n           <description>"+ Desc+"</description>\n           <styleUrl>#Magnifier</styleUrl>\n           <Point>\n              <altitudeMode>absolute</altitudeMode>\n              <coordinates>";
		all+= lon+","+lat+","+alt+"</coordinates>\n           </Point>\n       </Placemark>\n\n		";
		return all;
	}
	
	public void makeKmlFile(ArrayList<String> csvFile2){
		
		String CSVFile="C:/Users/Yarden/Desktop/New folder//new1.kml";
		PrintWriter pw = null;
		try {
			pw  = new PrintWriter(new File(CSVFile));
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		}
	}
	
	
	private String kmlWifiCoordinateGenerator(double lon,double lat,double alt,String pointName, String Desc){// adds one point to the kml - used for the wifi networks (with description)
		String all = "<Placemark>\n           <name>"+pointName+"</name>\n           <description>"+ Desc+"</description>\n           <styleUrl>#wifi</styleUrl>\n           <Point>\n              <altitudeMode>absolute</altitudeMode>\n              <coordinates>";
		all+= lon+","+lat+","+alt+"</coordinates>\n           </Point>\n       </Placemark>\n\n		";
		return all;
	}
	private String AddFilteringArea(String[] min, String[] max, String kml){//adds the rectangle of the filtering area to the kml
		kml+="<Placemark>\n      <name>Filtered Area</name>\n      <styleUrl>#transBluePoly</styleUrl>\n      <Polygon>\n        <extrude>1</extrude>\n        <outerBoundaryIs>\n          <LinearRing>\n            <coordinates>\n              " +min[0]+","+min[1]+",0\n              "+min[0]+","+max[1]+",0\n              "+max[0]+","+max[1]+",0\n              "+max[0]+","+min[1]+",0\n            </coordinates>\n          </LinearRing>\n        </outerBoundaryIs>\n      </Polygon>\n    </Placemark>";
		return kml;
	}
}
