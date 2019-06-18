def set_environments_secrets()
{
  super_variables = evaluate "datas.${branch_config}.secret"
  for ( yaml_variables in super_variables )
  {
    for ( yaml_variable in yaml_variables ) {
      evaluate "env.${yaml_variable.name} = sh(script: '''gpg --pinentry-mode loopback --batch --passphrase ${passphrase} --decrypt ${folder}/secret/${yaml_variable.filename} ''', returnStdout: true).trim()"
    }
  }
}
return this
