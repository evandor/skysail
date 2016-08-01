@menus
Feature: root menu specific features

  skysail server provides a menu API, returning tree-like structures of menu entries. 
  The top-level endpoint returns the names of root menus (like 'mainnav', 'sidenav').
  skysail bundles can contribute menu items or even start their own root menus. 
  
  @Equality
  Scenario: In a minimal skysail installation, the top-level endpoint returns at least 'mainnav'
    Given 1An Entity with name 'note'
	  And 1Another Entity with name 'note'
    Then 1the two entities are equal.

