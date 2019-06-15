def maven_test(def settings="null")
{
/*
  Funcion que ejecuta maven verify recibe el archivo settings.xml de fomar opcional si no se pasa se ejecuta sin el parametro -s.
*/
  try
  {
    stage("Maven test")
    {
      if ( settings == "null" )
      {
        sh "mvn test -X"
      }
      else
      {
        sh "mvn test -s ${settings} -X"
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
