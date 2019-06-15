def prueba()
{
    try
  {
    stage("prueba")
    {
      echo "esto es un stage de prueba"
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

