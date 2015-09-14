
(function(){
    var app = angular.module('runnersNotes',[]);

    app.controller('NoteController',['$http',function($http){
        var rn = this;
        rn.success = false;
        rn.error = false;
        rn.notes = [];
        rn.note = {};
        $http.get('http://localhost:8080/notes').success(function(data){
            	rn.notes = data.items;
        });

        this.addNote = function() {
            rn.note.created = new Date(rn.note.date).getTime();
            $http.post('http://localhost:8080/notes',rn.note).then(function(response){
                if (response.status === 201) {
                    rn.notes.push(rn.note);
                    rn.note = {};
                    rn.success = true;
                    rn.error = false;
                    $("#addForm").collapse('hide');
                } else {
                    rn.success = false;
                    rn.error = true;
                }
            });
        };
    }]);
})();