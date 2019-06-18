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
return this
