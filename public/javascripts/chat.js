var app = angular.module('chatApp', ['ngMaterial']);

app.controller('chatController', function ($scope) {
    $scope.messages = [
        {
            'sender': 'USER',
            'text': 'Hello'
        },
        {
            'sender': 'BOT',
            'text': 'Hello what ca i do for you'
        },
        {
            'sender': 'USER',
            'text': 'Buy me a pizza'
        },
        {
            'sender': 'BOT',
            'text': 'Sure'
        }
    ];

});