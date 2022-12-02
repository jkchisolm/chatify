const AUTHORIZE = "https://accounts.spotify.com/authorize";
const TOKEN = "https://accounts.spotify.com/api/token";
const TOPTRACKS = "https://api.spotify.com/v1/me/top/tracks?time_range=short_term&limit=10&offset=0";
const TOPTRACKSLONG = "https://api.spotify.com/v1/me/top/artists?time_range=long_term&limit=15&offset=0";
const TOPARTISTS = "https://api.spotify.com/v1/me/top/artists?time_range=long_term&limit=10&offset=0";

var client_id = "99c9c603516948c98cbe088a6306be21"; // chatify
var client_secret = "f9a787a2c6824a0898fb97ac3d2a6bb5";
var redirect_uri = "http://localhost:8080/chatify-webapp/signup.html"; // get URI
var access_token = null;
var refresh_token = null;

// JSON string that contains an array "items" of objects 
// each object contains track title, artist name, and album cover image url
var topTenTracks = "";

// JSON string that contains an array "items" of objects
// each object contains artist name and artist image url
var topTenArtists = "";

// JSON string
// strings containing genre
var topFiveGenres = ""; 
const genreMap = new Map(); // key = genre; value = number of times it appears


function onPageLoad() {
    if (window.location.search.length > 0) {
        handleRedirect();
    } else {
        if (access_token == null) {
            // we don't have an access token so present token section
            document.getElementById("tokenSection").style.display = 'block';
        } else {
            // we have an access token so move on
            document.getElementById("loginSection").style.display = 'block';
            getUserTopArtists();
            getUserTopTracks();
            getUserTopGenres();
        }
    }
}

function handleRedirect() {
    let code = getCode();
    fetchAccessToken(code);
    window.history.pushState("", "", redirect_uri); // remove param from url
}

function getCode() {
    let code = null;
    const queryString = window.location.search;
    if (queryString.length > 0) {
        const urlParams = new URLSearchParams(queryString);
        code = urlParams.get('code')
    }
    return code;
}

function requestAuthorization() {
    let url = AUTHORIZE;
    url += "?client_id=" + client_id;
    url += "&response_type=code";
    url += "&redirect_uri=" + encodeURI(redirect_uri);
    url += "&show_dialog=true";
    url += "&scope=user-top-read";
    window.location.href = url; // show spotify's authorization screen
}

function fetchAccessToken(code) {
    let body = "grant_type=authorization_code";
    body += "&code=" + code;
    body += "&redirect_uri=" + encodeURI(redirect_uri);
    body += "&client_id=" + client_id;
    body += "&client_secret=" + client_secret;
    callAuthorizationApi(body);
}

function callAuthorizationApi(body) {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", TOKEN, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xhr.setRequestHeader('Authorization', 'Basic ' + btoa(client_id + ":" + client_secret));
    xhr.send(body);
    xhr.onload = handleAuthorizationResponse;
}

function handleAuthorizationResponse() {
    if (this.status == 200) {
        var data = JSON.parse(this.responseText);
        // console.log(data);
        var data = JSON.parse(this.responseText);
        if (data.access_token != undefined) {
            access_token = data.access_token;
        }
        if (data.refresh_token != undefined) {
            refresh_token = data.refresh_token;
        }
        onPageLoad();
    } else {
        console.log(this.responseText);
        alert(this.responseText);
    }
}

function refreshAccessToken() {
    let body = "grant_type=refresh_token";
    body += "&refresh_token=" + refresh_token;
    body += "&client_id=" + client_id;
    callAuthorizationApi(body);
}

function callApi(method, url, body, callback) {
    let xhr = new XMLHttpRequest();
    xhr.open(method, url, true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.setRequestHeader('Authorization', 'Bearer ' + access_token);
    xhr.send(body);
    xhr.onload = callback;
}

// get user's recent favorite tracks
function getUserTopTracks() {
    callApi("GET", TOPTRACKS, null, userTopTracks); // short_term
}

// get user's top 5 genres of all time
function getUserTopGenres() {
    callApi("GET", TOPTRACKSLONG, null, userTopArtists); //long_term
}

// get user's favorite artists of all time
function getUserTopArtists() {
    callApi("GET", TOPARTISTS, null, userTopArtists); //long_term
}

// get user's all-time favorite artists (name and image)
function userTopArtists() {
    if (this.status == 200) {
        var data = JSON.parse(this.responseText);
        const items = data.items; //array of JSON objects 
        const topTen = [];
        
        // top artists 
        if(items.length == 10) {
            for (let i = 0; i < items.length; i++) {

                // parse each JSON object in the items array
                var nameVar = "";
                var imageUrl = "";
    
                // get artist's name
                nameVar = (items[i]).name;
    
                // get artist image
                const images = (items[i]).images;
                if( (images != undefined) && (images.length != 0) ) {
                    imageUrl = images[0].url;
                }
    
                // add artist's info to list
                const obj = {name: nameVar, image: imageUrl};
                // topTen.push(JSON.stringify(obj)); // <-- JSON OF JSON OBJECTS
                topTen.push(obj); // <-- JSON OF OBJECTS
            }
            const obj = {items: topTen};
            topTenArtists = JSON.stringify(obj);

            console.log(topTenArtists);
            var d = JSON.parse(topTenArtists);
            var i = d.items;
            console.log(i[1].name);
        }
        else if(items.length == 15) {

            // get genres and the number of times they occur
            for(let i = 0; i < 15; i++) {
                // console.log(items[i]);
                const genres = (items[i]).genres; // array of strings
                for (let j = 0; j < genres.length; j++) {
                    if (genreMap.has(genres[j])) {
                        genreMap.set( genres[j], (genreMap.get(genres[j]) + 1) ); 
                    }
                    else {
                        genreMap.set(genres[j], 1);
                    }
                }
            }

            // sort genres (top 5 only) from 1 to 5
            const topFiveKey = [];
            for(const [key, value] of genreMap) {

                var max = 0; 
                var theKey = "";
                for (const [key, value] of genreMap) {
                    if(value > max) { 
                        max = value;
                        theKey = key;
                    }
                }
                topFiveKey.push(theKey);
                genreMap.delete(theKey);
                if(topFiveKey.length == 5) break;
            }

            const obj = {one: topFiveKey[0], two: topFiveKey[1], three: topFiveKey[2], four: topFiveKey[3], five: topFiveKey[4]};
            topFiveGenres = JSON.stringify(obj);

            console.log(topFiveGenres);
        }
    } else if (this.status == 401) {
        refreshAccessToken();
    } else {
        console.log(this.responseText);
        alert(this.responseText);
    }
}

// get user's top tracks
function userTopTracks() {
    if (this.status == 200) {
        var data = JSON.parse(this.responseText);
        const items = data.items; //array of JSON objects (tracks)
        const topTen = [];
        
        // top 10 songs in the past 4 weeks
        for (let i = 0; i < 10; i++) {
            // const temp = []; 

            // get the track title
            var trackTitle = (items[i]).name;

            // get array of all artists' names
            const artists = (items[i]).artists; 
            var artistName = artists[0].name; // only get the first artists' name for simplicity

            // get URL of album cover
            const album = items[i].album;
            const images = album.images;
            var imageUrl = images[0].url;

            // add array of track info to the list
            const obj = {track: trackTitle, artist: artistName, image: imageUrl};
            // topTen.push(JSON.stringify(obj)); // <-- JSON OF JSON OBJECTS
            topTen.push(obj);
        }
        const obj = {items: topTen};
        topTenTracks = JSON.stringify(obj);
        console.log(topTenTracks);
    } 
    else if (this.status == 401) {
        refreshAccessToken();
    } 
    else {
        console.log(this.responseText);
        alert(this.responseText);
    }
}
