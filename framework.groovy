env.pipeline_folder="pipeline"
env.dir_framework = "${folder}/framework/"
env.global_framework="${dir_framework}/global_framework.groovy"
def load_framework()
{
  /*
    la variable folder tiene que ser seteada en el Jenkinsfile si no no va a funcionar
  */
  // CLEAN DE GLOBAL STAGES
  sh """
      #!/bin/bash
      if [ -e "${global_framework}" ] ; then
        rm ${global_framework}
      fi
  """
  // CHARGE GLOBAL STAGES
  final foundFiles = sh(script: 'find ${dir_framework} -type f', returnStdout: true).split()
  foundFiles.each {
    print it
    sh """
      #!/bin/bash
      cat ${it} >> ${global_framework}
    """
  }
  // LOAD ALL STAGES
  framework = load "${global_framework}"
}
def main()
{
  load_framework()
  framework.cargo_stages()
  framework.read_yaml()
  framework.set_global_environment()
  framework.set_branch()
  if ( framework.credentials_to_variable("passphrase", "passphrase") )
  {
    framework.set_environments_secrets()
  }
  else
  {
    echo "si hay secrets no se van a utilizar por que no existe el secret passphrase generar este secret en el jenkins para poder desifrar los secrets"
  }
  framework.set_environments()
  framework.exec_stages()
}
return this
