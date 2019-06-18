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
return this
