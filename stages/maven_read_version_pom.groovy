def maven_read_version_pom()
{
/*
  Funcion 
*/
  try
  {
    stage("Maven read version fron pom")
    {
      pom = readMavenPom file: 'pom.xml'
      env.pom_version = pom.version
    }
  }
  catch ( e )
  {
    echo "Error encountered: ${e}"
    currentBuild.result = 'FAILURE'
    throw err
  }
}
return this
