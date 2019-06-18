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
return this
