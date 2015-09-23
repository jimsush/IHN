function makeId(parentId, id, type){
    if(!parentId)
        return id+"#"+type;
    var pos=parentId.indexOf('#');
    return parentId.substring(0, pos)+"_"+id+"#"+type;
}

function getType(fullId){
    var pos=fullId.indexOf('#');
    return fullId.substring(pos+1);
}

function getParentId(fullId){
    var pos=fullId.lastIndexOf('-');
    return fullId.substring(0, pos);
}


var ihn = {};

ihn.Naming = function(){
    var selfId;
    var parentId;
    var type;
    var fullId;
};

ihn.Naming.prototype.makeId = function(parentId, id, type){
    this.parentId=parentId;
    this.selfId=id;
    this.type=type;
    if(!parentId){
        this.fullId=id+"#"+type;
    }else{
        var pos=parentId.indexOf('#');
        this.fullId=parentId.substring(0, pos)+"_"+id+"#"+type;
    }
    return this.fullId;
};

ihn.Naming.prototype.getType=function(){
    var pos=this.fullId.lastIndexOf('-');
    return this.fullId.substring(0, pos);
};

ihn.Naming.prototype.getParentId=function(){
    var pos=this.fullId.lastIndexOf('-');
    return this.fullId.substring(0, pos);
};
