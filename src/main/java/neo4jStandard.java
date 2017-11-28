import org.neo4j.driver.v1.*;

import java.util.Scanner;

public class neo4jStandard {

    public static Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("admin1", "password"));
    public static Session session = driver.session();


    public void createPlanet(String planetName, String... landmarks){
            session.run("CREATE (n:Planet {name: \""+planetName+"\", landmarks: \"\"})");

            for(String landmark : landmarks) {
                session.run("MATCH (n:Planet) SET n.landmarks = n.landmarks +  [\"" +landmark+ "\"]");
            }
    }

    public void updateLandmarksPropertiesForNode(){
        Scanner in = new Scanner(System.in);
        System.out.print("Name of node: ");
        String planetName = in.next();

        System.out.println("Do you want to change name? (y/n)");
        String nameChangeAsString = in.next();
        boolean nameChange = nameChangeAsString.equals("y");

        if(nameChange){
            System.out.println("Enter new name: ");
            String newName = in.next();
            session.run("MATCH (n:Planet {name: \"" +planetName+ "\"}) SET n.name = \""+newName+"\"");
        }
    }

    public void createRelationship(String relationship, String nodeFrom, String nodeTo){
        session.run("MATCH (p1:Planet {name: \""+nodeFrom+"\"}), (p2:Planet {name: \""+nodeTo+"\"}) CREATE (p1)-[r:"+relationship+"]->(p2) RETURN r");

    }

    public StatementResult getAllNodes(){
        StatementResult result = session.run("MATCH (n:Planet) RETURN n.name AS name, n.landmarks AS landmarks");
        printNodeAndItsProperties(result);
        return result;
    }

    public StatementResult getSpecificNode(String planetName){
        StatementResult result = session.run("MATCH (n:Planet {name: \""+planetName+"\"}) RETURN n.name AS name, n.landmarks AS landmarks");
        printNodeAndItsProperties(result);
        return result;
    }

    public void ensureForceFlagIsSetAndIfSoDelete(boolean force, String planetName){
        if (!force) {
            System.out.println("FAILURE: Could not delete nodes");
        } else if (force && planetName.equals("")) {
            session.run("MATCH (n) DETACH DELETE n");
        }
        else {
            session.run("MATCH (n:Planet {name: \"" + capitalize(planetName) + "\"}) DETACH DELETE n");
        }
    }

    public void deleteAllNodes(){
        System.out.println("Are you sure you want to delete all nodes?");
        Scanner in = new Scanner(System.in);
        String result = in.next();
        boolean force = (result.equals("Yes"));
        ensureForceFlagIsSetAndIfSoDelete(force, "");
    }

    public void deleteSpecificNode(String planetName){
        System.out.println("Are you sure you want to delete "+planetName+ "? (y/n)");
        Scanner in = new Scanner(System.in);
        String result = in.next();
        boolean force = (result.equalsIgnoreCase("y"));
        ensureForceFlagIsSetAndIfSoDelete(force, planetName);
    }

    public void printNodeAndItsProperties(StatementResult result){
        while(result.hasNext()){
            Record record = result.next();
            System.out.println("Planet: " + record.get("name") + " = " + record.get("landmarks"));

        }
    }

    public void startPlanetConfiguration(){
        boolean isFinished = false;
      while(!isFinished) {
          System.out.println("\nEnter CREATE, DELETE, UPDATE, VIEW, RELATIONSHIP or EXIT:");
          Scanner in = new Scanner(System.in);
          String action = in.next();
          action = action.toLowerCase();

          if (action.equals("create")) {
              System.out.println("Enter name of planet");
              String planetName = in.next();
              createPlanet(planetName);
              getSpecificNode(planetName);
          } else if (action.equals("delete")) {
              System.out.println("Delete all (A) or a specific planet (S)");
              String deleteOption = in.next();

              if (deleteOption.equalsIgnoreCase("a")) {
                  deleteAllNodes();
              } else if (deleteOption.equalsIgnoreCase("s")) {
                  System.out.println("Enter name of planet to delete: ");
                  String planetName = in.next();
                  deleteSpecificNode(planetName);
              }
          } else if (action.equals("update")) {
              updateLandmarksPropertiesForNode();
          } else if (action.equals("view")) {
              System.out.println("View all (A) or a specific planet (S)");
              String viewChoice = in.next();

              if (viewChoice.toLowerCase().equals("a")) {
                  getAllNodes();
              } else if (viewChoice.toLowerCase().equals("s")) {
                  System.out.println("Enter planet you want to view: ");
                  String planet = in.next();
                  getSpecificNode(capitalize(planet));
              }
          } else if (action.equals("relationship")) {
              System.out.println("Enter first planet: ");
              String firstPlanet = in.next();
              System.out.println("Enter second planet: ");
              String secondPlanet = in.next();
              System.out.println("Enter relationship: ");
              String relationship = in.next();
              createRelationship(relationship, firstPlanet, secondPlanet);
          }
          else if (action.equals("exit")){
              isFinished = true;
          }
      }
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static void main(String[] args) {

        neo4jStandard mySession = new neo4jStandard();
        mySession.startPlanetConfiguration();

        session.close();
        driver.close();
    }
}