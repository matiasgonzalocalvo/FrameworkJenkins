def cargo_stages()
{
  /*
    la variable folder tiene que ser seteada en el Jenkinsfile si no no va a funcionar
  */
  env.global_stages="${folder}/global_stages.groovy"
  env.dir_stages = "${folder}/stages"
  // CLEAN DE GLOBAL STAGES
  sh """
      echo "" > ${global_stages}
  """
  // CHARGE GLOBAL STAGES
  final foundFiles = sh(script: 'find ${dir_stages} -type f', returnStdout: true).split()
  foundFiles.each {
    print it
    sh """
      #!/bin/bash
      cat ${it} >> ${global_stages}
    """
  }
  // LOAD ALL STAGES
  stages = load "${global_stages}"
}
return this
