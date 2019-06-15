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
def read_yaml()
{
  env.yaml_name = sh(script: ''' cat .git/config |grep -i url |awk -F"/" '{printf $NF}'|sed 's/.git/.yaml/g' ''', returnStdout: true)
  datas = readYaml file: "${folder}/configuration/${yaml_name}"
}
def set_environments()
{
  super_variables = evaluate "datas.${branch_config}.env"
  for ( yaml_variables in super_variables )
  {
    for ( yaml_variable in yaml_variables ) {
      evaluate "env.${yaml_variable.name}=\"${yaml_variable.value}\""
    }
  }
}
def exec_stages()
{
  super_stages = evaluate "datas.${branch_config}.stages"
  for ( stages in super_stages )
  {
    for ( stage in stages )
    {
      evaluate "stages.${stage.stage}()"
    }
  }
}
def set_branch()
{
  for ( i in datas )
  {
    for ( j in i )
    {
      if ( j.key != "global" ) 
      {
        for ( brancheo in j )
        {
          if ( brancheo.key == BRANCH_NAME ) 
          {
            env.branch_config="${brancheo.key}"
            break;
          }
          else
          {
            for ( b_wildcard in brancheo.value )
            {
              for ( wildcard in b_wildcard.value ) 
              {
                if ( BRANCH_NAME =~ wildcard.branch )
                {
                  env.branch_config="${brancheo.key}"
                  break;
                }
              }
            }
          }
        }
      }
    }
  }
}
def set_global_environment()
{
  super_variables = evaluate "datas.global.env"
  for ( yaml_variables in super_variables )
  {
    for ( yaml_variable in yaml_variables ) {
      //echo "yaml_variable.name == ${yaml_variable.name} || yaml_variable.value == ${yaml_variable.value}"
      evaluate "env.${yaml_variable.name}=\"${yaml_variable.value}\""
    }
  }

}
def main()
{
  cargo_stages()
  read_yaml()
  set_global_environment()
  set_branch()
  set_environments()
  exec_stages()
  sh "export"
}
return this
