# neo4j-example



==============================================
            Cloning Repos
==============================================

git clone https://github.com/daraghhiggins1/neo4j-example.git


=============================================
            Installation of neo4j
=============================================

Windows:        
  Download Community Edition .exe installed on https://neo4j.com/
  Start the installer
  Choose a database location          
  Usage: 
          Open neo4j Community Edition and click 'Start' to start the server
          Keep this running to allow execution through Java API
          Access localhost:7474 to access neo4j's in-built browse application
                 
Linux:
  Same as the first step for Windows above except download the .tar instead
  Run the following commands to extract the file
  cd path/to/tar/package
  tar -xf neo4j-community-3.2.6-unix.tar.gz
  Usage:
          Run the following commands to start the neo4j server
          cd path/to/neo4j/package
          sudo ./neo4j console
          Keep this running to allow execution through Java API
          Access localhost:7474 to access neo4j's in-built browse application

 
Note: Disregarding operating system, for login use the following credentials:
          Username: neo4j     Password: neo4j

=================================================
          Java API Usage
=================================================

Run neo4jStandard.java
