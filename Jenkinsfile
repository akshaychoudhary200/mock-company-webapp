pipeline {
  /*
   * TODO: Implement pipeline stages/steps
   *   See documentation: https://www.jenkins.io/doc/book/pipeline/syntax/#stages
   */
   node {
     stage("Clone the project") {
       git branch: 'main', url: ''
     }
     stage("Compilation") {
       sh "./mvnw clean install -DskipTests"
     }
     stage("Tests and Deployment") {
       stage("Runing unit tests") {
         sh "./mvnw test -Punit"
       }
       stage("Deployment") {
         sh 'nohup ./mvnw spring-boot:run -Dserver.port=8001 &'
       }
     }
   }
}
