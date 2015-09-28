function initFormPane(g3d, rightFormPane){
    rightFormPane.addRow(['开关:',
    {
      id: 'movable',
      checkBox: {
          label: '可移动',
          selected: false
      }
    },
    {
      id: 'editable',
      checkBox: {
          label: '可编辑',
          selected: false,
          onClicked : function(){
              var selected=rightFormPane.v('editable')
              g3d.setEditable(selected);
          }
      }
    }
    ], [80, 0.1+80, 0.1]);
    
    rightFormPane.addRow([null,
    {
      id: 'centerAxis',
      checkBox: {
          label: '中轴',
          selected: false,
          onClicked : function(){
              var selected=rightFormPane.v('centerAxis')
              g3d.setCenterAxisVisible(selected);                           
          }
      }
    },
    {
      id: 'wireframe',
      checkBox: {
          label: '线框图',
          selected: false,
          onClicked: function(){
                var selected=rightFormPane.v('wireframe')
                if(selected){
                    dataModel.each(function(data){
                        data.s({
                            'wf.visible': 'selected',
                            'wf.color': 'red'                                        
                        });
                    });                                
                }else{
                    dataModel.each(function(data){
                        data.s({
                            'wf.visible': false                                       
                        });
                    });                               
                }                            
            }                     
          }
    }
    ], [80, 0.1+80, 0.1]);

		rightFormPane.addRow([null,
    {
      id: 'originAxis',
      checkBox: {
          label: '坐标轴',
          selected: false,
          onClicked: function(){
              var selected=rightFormPane.v('originAxis')
              g3d.setOriginAxisVisible(selected);                           
          }
      }
    },
    {
      id: 'grid',
      checkBox: {
          label: '显示网格',
          selected: false,
          onClicked: function(){
              var selected=rightFormPane.v('grid')
              g3d.setGridVisible(selected);
          }
      }
    }
    ], [80, 0.1+80, 0.1]);
    
    rightFormPane.addRow();
    rightFormPane.addRow(['操作:',
    {
        button: {
            label: '导为图片',
            onClicked: function(){
                var w = window.open();
                w.document.open();                            
                w.document.write("<img src='" + g3d.toDataURL(g3d.getView().style.background) + "'/>");
                w.document.close();
            }
        }
    },
    {
        button: {
            label: '清除车位',
            onClicked: function(){ 
            }
        }
    }
    ],[80, 0.1+80, 0.1]);

    rightFormPane.addRow([null,
    {
        button: {
            label: '停车占位',
            onClicked: function(){
            }
        }
    },
    {
        button: {
            label: '离开车位',
            onClicked: function(){ 
            }
        }
    }
    ], [80, 0.1+80, 0.1]);
    
    rightFormPane.addRow([null,
    {
        button: {
            label: '模拟告警',
            onClicked: function(){
            }
        }
    },
    {
        button: {
            label: '清除告警',
            onClicked: function(){ 
            }
        }
    }
    ], [80, 0.1+80, 0.1]);
    
    rightFormPane.addRow();
    rightFormPane.addRow(['搜索商家:',
    {
      id: 'keywords',
      textField: {
          text: '关键字'
      }
    },
    {
        button: {
            label: '搜索',
            onClicked: function(){
                
            }
        }
    }
    ], [80, 0.1+80, 0.1]);

		rightFormPane.addRow();
    rightFormPane.addRow(['创建车位:',
    {
        id: 'direction',
        checkBox: {
          label: '横向排列',  //Landscape/Portait
          selected: true
        }
    },
    {
      id: 'prefixId',
      textField: {
          label: 'Prefix',
          text: '前缀:如H0'
      }
    }
    ], [80, 0.1+80,  0.1]);
    
    rightFormPane.addRow([null,
    {
      id: 'rows',
      textField: {
          text: '行数'
      }
    },
    {
      id: 'columns',
      textField: {
          text: '列数'
      }
    }
    ], [80, 0.1+80, 0.1]);
    rightFormPane.addRow([null,
    {
        button: {
            label: '创建',
            onClicked: function(){
                
            }
        }
    }
    ], [80, 0.1]);
}