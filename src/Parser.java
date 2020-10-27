import Data.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Parser {

    public static void main(String[] args) {
        Parser instance = new Parser();
        instance.populateGames(args[0]);

        if(args.length > 1){
            instance.populateSleeves(args[1]);
        }




    }


    public void populateGames(String fileName)  {
        File gamesFile = new File(fileName);
        if(!gamesFile.exists()){
            assert(false);
        }
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(gamesFile);
            br = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            assert(false);
        }
        String currLine = null;
        Games theGames = new Games();
        theGames.data = new TreeMap<>();

        Map<String,Game> theMap = theGames.data;

        while(true){
            try {
                currLine = br.readLine();
            } catch (IOException e) {
                assert(false);
            }
            if(currLine == null){
                break;
            }

            String gameName = currLine.substring(0,currLine.indexOf(','));

            while(gameName.contains("^")){
               gameName =  gameName.replace("^",",");
            }



            String cardCount = currLine.substring(currLine.indexOf(",")+1,currLine.lastIndexOf(","));


            String SleeveName = currLine.substring(currLine.lastIndexOf(",")+1);
            while(SleeveName.contains("^")){
                SleeveName =  SleeveName.replace("^",",");
            }





            if(!theMap.containsKey(gameName)){
                Game currGame = new Game(gameName);
                theMap.put(gameName,currGame);

            }
            CountAndSize toAdd = new CountAndSize(cardCount,SleeveName);
            theMap.get(gameName).addCountSize(toAdd);

        }

        for(Game g : theMap.values()){
            System.out.println(g.toString());
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();


        File toOutputTo = new File("Games.json");
        try {
            toOutputTo.createNewFile();
            FileWriter myWriter = new FileWriter(toOutputTo);
            gson.toJson(theGames,myWriter);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void populateSleeves(String fileName){
        Sleeves theSleeves = new Sleeves();
        theSleeves.data = new TreeMap<>();
        Map<String,Sleeve> theMap = theSleeves.data;

        File gamesFile = new File(fileName);
        if(!gamesFile.exists()){
            assert(false);
        }
        BufferedReader br = null;
        try {
            FileReader fr = new FileReader(gamesFile);
            br = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            assert(false);
        }
        String currLine = null;

        while(true){
            try {
                currLine = br.readLine();

            } catch (IOException e) {
                e.printStackTrace();
            }
            if(currLine == null){
                break;
            }
            String SleeveName = currLine.substring(0,currLine.indexOf(","));
            currLine = currLine.substring(currLine.indexOf(",")+1);
            String SKU = currLine.substring(0,currLine.indexOf(","));
            currLine = currLine.substring(currLine.indexOf(",")+1);
            String width = currLine.substring(0,currLine.indexOf(","));
            currLine = currLine.substring(currLine.indexOf(",")+1);
            String height = currLine.substring(0,currLine.indexOf(","));
            currLine = currLine.substring(currLine.indexOf(",")+1);
            String cost = currLine.substring(0,currLine.indexOf(","));
            currLine = currLine.substring(currLine.indexOf(","));
            String URL = currLine.substring(1);

            while(SleeveName.contains("^")){
                SleeveName = SleeveName.replace("^",",");
            }

            Sleeve newSleeve = new Sleeve(SKU,width,height,cost,URL,SleeveName);
            theMap.put(SleeveName,newSleeve);


        }



        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String json = gson.toJson(theSleeves);
        File toOutputTo = new File("Sleeves.json");
        try {
            toOutputTo.createNewFile();
            FileWriter myWriter = new FileWriter(toOutputTo);
            myWriter.write(json);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

}
