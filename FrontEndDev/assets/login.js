const loginUser = () => {
  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  // verify that user has entered something
  if (username === "" || password === "") {
    alert("Please enter a username and password");
    return;
  }
  alert(
    "Logged in user (placeholder). Will redirect to login page 5 seconds after clicking OK."
  );
  setTimeout(() => {
    window.location.href = "countdown.html";
  }, 5000);
};
