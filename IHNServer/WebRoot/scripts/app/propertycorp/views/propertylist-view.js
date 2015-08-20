define([
  'text!templates/propertylist-template.html'
], function (PropertyListTemplate) {

    var PropertyListView = Backbone.View.extend({
    	
        template: _.template(PropertyListTemplate),
        
        initialize: function () {
            this.render();
            this.collection.bind("add", this.addOne);
        },
        
        render: function () {
            this.$el.html(this.template());
            return this;
        },
        
        events: {
        	'click input[type=button]' : 'doToggleMenu'
        },
        
        doToggleMenu: function(event){
        	alert('toggle event');
        },
        
        addOne: function(model){
        	$("#proplist").append("<li style='line-height:38px;'><a href='#'>"+model.get('name')+","+model.get('city')+","+model.get('address')+"</a></li>");
        } 
        
        
    });

    return PropertyListView;
});