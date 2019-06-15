Framework Jenkins
===================
## Jenkinsfile ##
  ```groovy
  node ("master")
  {
    stage('Checkout SCM')
    {
      checkout scm
    }
    stage ("load framework.groovy")
    {
      branch = "master"
      url_git = "https://github.com/matiasgonzalocalvo/FrameworkJenkins"
      env.folder = "devops"
      sh "mkdir -p ${folder}"
      dir ("${folder}")
      {
        git(
          url: "${url_git}",
          branch: "${branch}"
        )
      }
      devops = load "devops/framework.groovy"
      devops.main()
    }
  }
  ```

## Carpeta configuration ##
  Esta carpeta contiene los archivos yaml para la ejecucion de los jobs. 
  
  cada archivo debe tener el mismo nombre del repo+.yaml ej:
  
  url del repo = https://github.com/matiasgonzalocalvo/testjenkinsframework
  
  nombre del repo = testjenkinsframework
  
  nombre del yaml que contenga toda la ejecucion = testjenkinsframework.yaml

  ```yaml
  - global:
      env:
        - name: name_variable_gobal_1
          value: value_variable_gobal_1
        - name: name_variable_gobal_2
          value: value_variable_gobal_2
  - master:
      branch_wildcard:
        - branch: prod
      env:
        - name: name_variable1_master
          value: value_variable1_master
        - name: name_variable2_master
          value: value_variable2_master
      stages:
        - stage: maven_read_version_pom
        - stage: maven_install
  - develop:
      branch_wildcard:
        - branch: develop
        - branch: testing/*
        - branch: feature/*
      env:
        - name: name_variable1_develop
          value: value_variable1_develop
        - name: name_variable2_develop
          value: value_variable2_develop
      stages:
        - stage: maven_read_version_pom
        - stage: maven_install
  ```

| Global |  |
| --- | --- |
| Global | Aca se setean las variables |


| master |  |
| --- | --- |
| master | si el branch que se esta ejecutando coincide con el declarado se setean las environments y despues de ejecutan los stages |
| branch_wildcard | aca se ponen los wildcard como por ejemplo todos los branch feature se van a ejecutar con este|
| env | aca se setean las variables de entorno que se van a utilizar para el pipeline |
| stages| aca se setean los stages a ejecutar |

## Carpeta Stages ##
  En esta carpeta se tienen que subir archivo que adentro contengan los stages de la organizacion como por ejemplo 
  ```groovy
  def maven_install()
  {
  /*
    Funcion que ejecuta maven clean install 
  */
    try
    {
      stage("Maven install")
      {
        sh "mvn clean install -DskipTests -s ${settings} -X"
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
  ```
