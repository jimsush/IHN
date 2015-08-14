define([
  'collections/ihn/sidemenus-collection',
  'text!templates/ihn/sidemenu-template.html'
], function (SideMenuCollection, SideMenuTemplate) {
  
  var SideMenuView = Backbone.View.extend({
      
	  template: _.template(SideMenuTemplate),
      
	  initialize: function () {
        this.collection = new SideMenuCollection([
        	{id: '1', name: 'demo', link: '#demo'}
        ]);
        
        this.listenTo(this.collection, 'change', this.render);
        this.collection.trigger('change');
      },
      
      render: function () {
    	var menus = this.collection.toJSON();
        this.$el.html(this.template({menus: menus}));
        return this;
      }
      
  });

  return SideMenuView;
});