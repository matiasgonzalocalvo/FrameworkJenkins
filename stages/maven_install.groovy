def maven_install(def settings="null")
{
/*
  Funcion que ejecuta maven verify recibe el archivo settings.xml de fomar opcional si no se pasa se ejecuta sin el parametro -s.
*/
  try
  {
    stage("Maven install")
    {
      if ( settings == "null" )
      {
        sh "mvn clean install -DskipTests -X"
      }
      else
      {
        sh "mvn clean install -DskipTests -s ${settings} -X"
      }
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
