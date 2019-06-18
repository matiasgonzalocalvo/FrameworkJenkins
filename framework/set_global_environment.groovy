def set_global_environment()	
{	
  for ( yaml_variables in datas.global.env )	
  {	
    for ( yaml_variable in yaml_variables ) {	
      evaluate "env.${yaml_variable.name}=\"${yaml_variable.value}\""	
    }	
  }	

}
return this
