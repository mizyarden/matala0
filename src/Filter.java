

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.Line;

public class Filter {

	public static void filtercsvFileByTime(String file, int startYear, int startMonth, int startDay, int startHour, int startMins,
			int startSecs,  int endYear, int endMonth, int endDay, int endHour, int endMins,
			int endSecs){
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br= new BufferedReader(fr); 
			int count=0;
			ArrayList<String> filteredCSV = new ArrayList<String>();
			String brLine = br.readLine();
			String[] line = brLine.split(",");
			String time = line[0];
			int year = Integer.parseInt(time.substring(0, 4));
			int month = Integer.parseInt(time.substring(5, 7));
			int day = Integer.parseInt(time.substring(8, 10));
			int hours = Integer.parseInt(time.substring(10, 13));
			int mins = Integer.parseInt(time.substring(13, 17));
			int secs = Integer.parseInt(line[0].substring(17, line[0].length()));
			while(brLine != null){
				if(Integer.parseInt(time.substring(0, 4))>=startYear && Integer.parseInt(time.substring(0, 4))<=endYear 
						&& Integer.parseInt(time.substring(5, 7))>=startMonth && Integer.parseInt(time.substring(5, 7))<=endMonth
						&& Integer.parseInt(time.substring(8, 10))>=startDay && Integer.parseInt(time.substring(8, 10))<=endDay 
						&& Integer.parseInt(time.substring(10, 13))>=startHour && Integer.parseInt(time.substring(10, 13))<=startHour
						&& Integer.parseInt(time.substring(13, 17))>=startMins && Integer.parseInt(time.substring(13, 17))<=endMins 
						&& Integer.parseInt(time.substring(13, 17))>=startSecs && Integer.parseInt(time.substring(13, 17))<=endSecs) 
				{
					filteredCSV.add(brLine);
				}
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}

