define([
  'text!templates/propertylist-template.html',
  'jquery'
], function (PropertyListTemplate,$) {

    var PropertyListView = Backbone.View.extend({
    	
        template: _.template(PropertyListTemplate),
        
        initialize: function () {
            this.render();
            this.listenTo(this.collection,'reset', this.resetAll);
            this.listenTo(this.collection,'add', this.addOne);
        },
        
        render: function () {
            this.$el.html(this.template());
            return this;
        },
        
        events: {
        	'click #btnMenu' : 'doToggleMenu'
        },
        
        doToggleMenu: function(event){
        	//alert('toggle event');
        	var e = $('#btnMenu');
            if(e.style.display == 'block')
               e.style.display = 'none';
            else
               e.style.display = 'block';
        },
        
        resetAll : function(){
        	this.collection.each(function(model){
        		$("#proplist").append("<li style='line-height:38px;'><a href='#'>"+model.get('name')+","+model.get('city')+","+model.get('address')+"</a></li>");
        	});
        },
        
        addOne: function(model){
        	$("#proplist").append("<li style='line-height:38px;'><a href='#'>"+model.get('name')+","+model.get('city')+","+model.get('address')+"</a></li>");
        } 
        
    });

    return PropertyListView;
    
});