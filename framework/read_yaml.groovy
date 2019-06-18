def read_yaml()
{
  env.yaml_name = sh(script: ''' cat .git/config |grep -i url |awk -F"/" '{printf $NF}'|sed 's/.git/.yaml/g' ''', returnStdout: true)
  datas = readYaml file: "${folder}/${pipeline_folder}/${yaml_name}"
}
return this
