例子如下：


<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE dynamic-hibernate-statement PUBLIC 
    "-//LANDRAY/SQL Hibernate Dynamic Statement DTD 1.0//EN"
    "http://www.dynamic.com/dtd/dtd/dynamic-hibernate-statement-1.0.dtd">
    
<dynamic-hibernate-statement>
	<hql-query name="resource.getChildren">
	<![CDATA[
		from Resource where parent.id=${parentId} and parent.id != id
	]]> 
	</hql-query>
	<hql-query name="resource.getRoots">
	<![CDATA[
		from Resource where parent.id = id order by orderIndex
	]]> 
	</hql-query>
	
	<sql-query name="resource.getDescendants">
	<![CDATA[
		select distinct t.id,
		                t.name,
		                t.description,
		  from user u
		 where ur.user_id = ${userId!}
		 <#if type == '1'>
		 	and u.type=1
		 	<#else>
		 	and u.type=0
		 </#if>
		   and u.type =  ${type!}
		   and u.status =  ${status!}
		 start with u.code = '${code!}'
	]]> 
	</sql-query>
</dynamic-hibernate-statement>