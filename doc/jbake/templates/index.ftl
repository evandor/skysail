<#include "header.ftl">
	
	<#include "menu.ftl">

	<div class="page-header">
		<h1>skysail technical documentation</h1>
		<p>This is the starting page for getting information about technical details of skysail.</p>
		<p>To get an idea of what skysail is at all, have a look at the <a href='/about.html'>about</a> section.</p>
	</div>
	
	<#list posts as post>
  		<#if (post.status == "published")>
  			<a href="${post.uri}"><h1><#escape x as x?xml>${post.title}</#escape></h1></a>
  			<p>${post.date?string("dd MMMM yyyy")}</p>
  			<p>${post.body}</p>
  		</#if>
  	</#list>
	
	<hr />
	
	<p>Older posts are available in the <a href="${config.archive_file}">archive</a>.</p>

<#include "footer.ftl">