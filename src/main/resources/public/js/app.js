
(function(){
    var app = angular.module('runnersNotes',[]);

    app.controller('NoteController',['$http',function($http){
        var rn = this;
        rn.notes = [];
        rn.note = {};
        $http.get('http://localhost:8080/notes').success(function(data){
            	rn.notes = data.items;
        });

        this.addNote = function() {
            rn.note.created = new Date(rn.note.date).getTime();
            $http.post('http://localhost:8080/notes',rn.note).then(function(){
                rn.notes.push(rn.note);
                rn.note = {};
            });
        };
    }]);
})();