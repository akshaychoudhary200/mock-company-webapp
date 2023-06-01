pipeline {
  /*
   * TODO: Implement pipeline stages/steps
   *   See documentation: https://www.jenkins.io/doc/book/pipeline/syntax/#stages
   */
   node {
     stage("Build") {
       sh "./gradlew assemble"
     }
     stage("Tests") {
       stage("Runing unit tests") {
         sh "./gradlew test"
       }
       stage("Deployment") {
         sh 'nohup ./gradlew spring-boot:run -Dserver.port=8001 &'
       }
     }
   }
}
