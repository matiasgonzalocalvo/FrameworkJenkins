def credentials_to_variable(env_variable, env_credentials)
{
  /*
    Funcion recibe como primer parametro env_variable que va hacer el nombre de la variable y como segundo parametro el nombre del credential. guarda el contenido de la credential adentro del nombre que se le paso como primer parametro
  */
  try
  {
    withCredentials([string(credentialsId: "${env_credentials}" , variable: "variable")])
    {
      evaluate "env.${env_variable}=\"${variable}\""
    }
    return true
  }
  catch (e)
  {
    print e
    return false
  }
}
return this
