const AUTHORIZE = "https://accounts.spotify.com/authorize";
const TOKEN = "https://accounts.spotify.com/api/token";
const TOPTRACKS = "https://api.spotify.com/v1/me/top/tracks?time_range=short_term&limit=10&offset=0";
const TOPTRACKSLONG = "https://api.spotify.com/v1/me/top/tracks?time_range=long_term&limit=25&offset=0";
const TOPARTISTS = "https://api.spotify.com/v1/me/top/artists?time_range=long_term&limit=10&offset=0";

var client_id = "22c5bff3962849c0b67a578d315b5e24";
var client_secret = "9e6f35c06c1447deabb7035de0750f52";
var redirect_uri = "http://127.0.0.1:5500/index.html";
var access_token = null;
var refresh_token = null;

var mysql = require('mysql');

const topTenTracks = [];
const topTenArtists = [];
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
        console.log(data);
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
// track title, artist name, album cover
function getUserTopTracks() {
    callApi("GET", TOPTRACKS, null, userTopTracks); // short_term
}

// get user's favorite track of all time; use this to extract favorite genres
function getUserTopTracksLong() {
    callApi("GET", TOPTRACKSLONG, null, userTopTracks); //long_term
}

// get user's favorite artists of all time
// name and image of artist
function getUserTopArtists() {
    callApi("GET", TOPARTISTS, null, userTopArtists); //long_term
}

// get user's all-time favorite artists (name and image)
function userTopArtists() {
    if (this.status == 200) {
        var data = JSON.parse(this.responseText);
        const items = data.items; //array of JSON objects 
        for (let i = 0; i < items.length; i++) {

            // parse each JSON object in the items array
            var artist = JSON.parse(items[i]);
            const temp = [];

            // get artist's name
            temp.push(artist.name);

            // get artist image
            const images = artist.images;
            if( (images != undefined) && (images.length != 0) ) {
                const image = JSON.parse(images[0]);
                temp.push(image.url);
            }

            // add artist's info to list
            topTenArtists.push(temp);
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
        
        // top 10 songs in the past 4 weeks
        if(items.length == 10) {
            for (let i = 0; i < 10; i++) {
                const temp = []; 

                // parse each JSON object in the array; get top 10 songs
                var track = JSON.parse(items[i]);
                temp.push(track.name); // song title

                // get array of all artists' names
                const artists = track.artist; 
                const temp2 = [];
                for(let j = 0; j < artists.length; j++) {
                    var artist = JSON.parse(artists[j]);
                    temp2.push(artist.name); // push name of all artists on the track
                }
                temp.push(temp2);

                // get URL of album cover
                const album = JSON.parse(track.album)
                const images = album.images;
                if(images != undefined && (images.length != 0)) {
                    const image = JSON.parse(images[0]);
                    temp.push(image.url);
                }

                // add array of track info to the list
                topTenTracks.push(temp); 
            }
        }
        else if(items.length == 25) {

            // get genres and the number of times they occur
            for(let i = 0; i < 25; i++) {
                var track = JSON.parse(items[i]);
                const genres = track.genres; // array of strings
                for (let j = 0; j < genres.length; j++) {
                    if (genreMap.has(genres[j])) genreMap.get(genres[j]) = genreMap.get(genres[j]) + 1;
                    else genreMap.set(genres[j], 1);
                }
            }
        }
    } 
    else if (this.status == 401) {
        refreshAccessToken();
    } 
    else {
        console.log(this.responseText);
        alert(this.responseText);
    }
}
