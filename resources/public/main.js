var profile,
    google_id,
    id_token;
function onSignIn(googleUser) {
    profile = googleUser.getBasicProfile();
    id_token = googleUser.getAuthResponse().id_token;

    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/check_token');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.onload = function() {
        console.log('Signed in as: ' + xhr.responseText);
    };
    xhr.send('idtoken=' + id_token);
}

// gapi.load('auth2', function() {
//     var auth2 = gapi.auth2.getAuthInstance();

//   // Sign the user in, and then retrieve their ID.
//     auth2.signIn().then(function() {
//         google_id = auth2.currentUser.get().getId();
//         console.log(google_id);
//   });
// });
