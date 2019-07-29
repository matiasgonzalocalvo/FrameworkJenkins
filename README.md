Framework Jenkins
===================
## Roadmap ##
1 - Agregar nuevo modulo que se llame flow que englobe varios stages standar por ejemplo compilar + test unitarios + sonar + generar docker etc
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
      url_git = "https://github.com/matiasgonzalocalvo/testjenkinsframework"
      credentialsId = "matiasgonzalocalvo_github"
      env.folder = "devops"
      sh "mkdir -p devops"
      dir ("${folder}")
      {
        git(
          url: "${url_git}",
          credentialsId: "${credentialsId}",
          branch: "${branch}"
        )
      }
      devops = load "devops/framework.groovy"
      devops.main()
    }
  }
  ```

## Carpeta pipeline ##
  Esta carpeta contiene los archivos yaml para la ejecucion de los jobs. 
  
  cada archivo debe tener el mismo nombre del repo+.yaml ej:
  
  url del repo = https://github.com/matiasgonzalocalvo/testjenkinsframework
  
  nombre del repo = testjenkinsframework
  
  nombre del yaml que contenga toda la ejecucion = testjenkinsframework.yaml
  
  en la seccion global se cargan las variables que van a ser globales a todas las ejecuciones. 

  despues 

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
      secret:
        - name: prueba_secret
          filename: documento.asc
        - name: prueba2_secret
          filename: documento2.asc
      stages:
        - stage: maven_read_version_pom
        - stage: maven_install
  - develop:
      branch_wildcard:
        - branch: develop
        - branch: testing/*
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
| branch_wildcard | si se quiere que todos los branch que contenga el nombre feature se ejecute en la seccion master se debe agregar aca|
| env | aca se setean las variables de entorno que se van a utilizar para el pipeline |
| stages| aca se setean los stages a ejecutar el nombre del stage debe coincidir con el nombre de la funcion declarada en la carpeta stages|

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

## SECRETS ##
  para los secrets usamos cifrado asimetrico. primero hay que crear el par de llaves (publica y privada)
  crear un archivo con el nombre secrets.gnupg
  ```bash
     %echo Generating a basic OpenPGP key
     Key-Type: default
     Key-Length: 4096
     Subkey-Type: default
     Subkey-Length: 4096
     Name-Real: nombre-real
     Name-Comment: comentario
     Name-Email: email@gmail.com
     Expire-Date: 0
     Passphrase: password-Passphrase
     # Do a commit here, so that we can later print "done" :-)
     %commit
     %echo done
  ```
  y reemplazar los paramatros por los que corresponda
  
  luego ejecutar 
  ```bash
    gpg --batch --gen-key secrets.gnupg
  ```

  esco creara las llaves

  para exportar la llave publica ejecutar el siguiente comando 
  ```bash
    gpg --export --armor nombre-real > llave-publica.asc
  ```

  y para exportar la llave privada el siguiente comando

  ```bash
    gpg --armor --pinentry-mode loopback --batch --passphrase password-Passphrase --export-secret-keys nombre-real > llave-privada.asc 
  ```

  importar llave privada en el jenkins para poder desifrar los secrets 
  ```bash
    gpg --import --pinentry-mode loopback --batch --passphrase password-Passphrase llave-privada.asc
  ```
  
  confiar en el certificado recien instalado 
  ```bash
    (echo trust &&  sleep 2 && echo 5 && sleep 2 && echo y && sleep 1 && echo quit) | gpg --command-fd 0 --edit-key matiasgonzalocalvo@gmail.com
    gpgconf --kill gpg-agent
  ```

  verificar que se importo bien
  ```bash
    gpg --list-keys
  ```

  para crear secrets hay que crearlos con la llave publica y el unico que la va a poder desifrar es el que tenga la llave privada
  ```bash
    gpg --encrypt --armor --recipient  nombre-real  documento.txt
  ```

  esto va a generar un archivo que se llama documento.txt.asc ese archivo hay que moverlo adentro de la carpeta secret 
  despues hay que agregar en el yaml en la parte de secret el nombre de la variable que se va a cargar + el filename de donde se va a desencryptar el contenidovim 
