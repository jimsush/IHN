define([
], function() {
  
  var PropertyAsset=Backbone.Model.extend({
	  urlRoot:'/rest/park/properties',
	  defaults:{
		  id:'',
		  name:'',
		  corp:'',
		  type:'Parking',
		  floorNum:1,
		  floors:'B1',
		  city:'ShangHai',
		  address:'',
		  phone:'',
		  longitude:0,
		  latitude:0,
		  contact:'',
		  contactPhone:'',
		  description:''
	  },
	  
	  initialize : function(){
	  }
  });	
	
  var PropertyAssetCollection = Backbone.Collection.extend({
	  model: PropertyAsset,
	  url: '/rest/park/properties'
  });
  
  return PropertyAssetCollection;
  
});