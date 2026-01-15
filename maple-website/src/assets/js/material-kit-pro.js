function debounce(func, wait, immediate) {
  var timeout;
  return function () {
    var context = this,
      args = arguments;
    clearTimeout(timeout);
    timeout = setTimeout(function () {
      timeout = null;
      if (!immediate) func.apply(context, args);
    }, wait);
    if (immediate && !timeout) func.apply(context, args);
  };
}

function smoothToPricing(id) {
  if (document.getElementById(id)) {
    document.getElementById(id).scrollIntoView({
      behavior: "smooth"
    });
  }
}

function debounce(func, wait, immediate) {
  var timeout;
  return function () {
    var context = this,
      args = arguments;
    clearTimeout(timeout);
    timeout = setTimeout(function () {
      timeout = null;
      if (!immediate) func.apply(context, args);
    }, wait);
    if (immediate && !timeout) func.apply(context, args);
  };
}

var popoverTriggerList = [].slice.call(
  document.querySelectorAll('[data-bs-toggle="popover"]')
);
var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
  return new bootstrap.Popover(popoverTriggerEl);
});

var tooltipTriggerList = [].slice.call(
  document.querySelectorAll('[data-bs-toggle="tooltip"]')
);
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
  return new bootstrap.Tooltip(tooltipTriggerEl);
});

function setAttributes(el, options) {
  Object.keys(options).forEach(function (attr) {
    el.setAttribute(attr, options[attr]);
  });
}

var popoverTriggerList = [].slice.call(
  document.querySelectorAll('[data-toggle="popover"]')
);
var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
  return new bootstrap.Popover(popoverTriggerEl);
});

var tooltipTriggerList = [].slice.call(
  document.querySelectorAll('[data-toggle="tooltip"]')
);
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
  return new bootstrap.Tooltip(tooltipTriggerEl);
});

window.onload = function () {

  var inputs = document.querySelectorAll("input");

  for (var i = 0; i < inputs.length; i++) {
    inputs[i].addEventListener(
      "focus",
      function (e) {
        this.parentElement.classList.add("is-focused");
      },
      false
    );

    inputs[i].onkeyup = function (e) {
      if (this.value != "") {
        this.parentElement.classList.add("is-filled");
      } else {
        this.parentElement.classList.remove("is-filled");
      }
    };

    inputs[i].addEventListener(
      "focusout",
      function (e) {
        if (this.value != "") {
          this.parentElement.classList.add("is-filled");
        }
        this.parentElement.classList.remove("is-focused");
      },
      false
    );
  }

  var ripples = document.querySelectorAll(".btn");

  for (var i = 0; i < ripples.length; i++) {
    ripples[i].addEventListener(
      "click",
      function (e) {
        var targetEl = e.target;
        var rippleDiv = targetEl.querySelector(".ripple");

        rippleDiv = document.createElement("span");
        rippleDiv.classList.add("ripple");
        rippleDiv.style.width = rippleDiv.style.height =
          Math.max(targetEl.offsetWidth, targetEl.offsetHeight) + "px";
        targetEl.appendChild(rippleDiv);

        rippleDiv.style.left = e.offsetX - rippleDiv.offsetWidth / 2 + "px";
        rippleDiv.style.top = e.offsetY - rippleDiv.offsetHeight / 2 + "px";
        rippleDiv.classList.add("ripple");
        setTimeout(function () {
          rippleDiv.parentElement.removeChild(rippleDiv);
        }, 600);
      },
      false
    );
  }
};

function dropDown(a) {
  if (!document.querySelector(".dropdown-hover")) {
    event.stopPropagation();
    event.preventDefault();
    var multilevel = a.parentElement.parentElement.children;

    for (var i = 0; i < multilevel.length; i++) {
      if (multilevel[i].lastElementChild != a.parentElement.lastElementChild) {
        multilevel[i].lastElementChild.classList.remove("show");
        multilevel[i].firstElementChild.classList.remove("show");
      }
    }

    if (!a.nextElementSibling.classList.contains("show")) {
      a.nextElementSibling.classList.add("show");
      a.classList.add("show");
    } else {
      a.nextElementSibling.classList.remove("show");
      a.classList.remove("show");
    }
  }
}

if (document.querySelector(".blur-shadow-image")) {
  var shadowCards = document.querySelectorAll(".blur-shadow-image");
  var shadowCardsRounded = document.querySelectorAll(
    ".blur-shadow-image.rounded-circle"
  );

  if (shadowCardsRounded) {
    for (var i = 0; i < shadowCardsRounded.length; i++) {
      var div = document.createElement("DIV");
      shadowCardsRounded[i].parentElement.appendChild(div);
      div.classList.add("colored-shadow", "rounded");

      var currentSrc = shadowCardsRounded[i].children[0].getAttribute("src");
      var el = shadowCardsRounded[i].nextElementSibling;

      el.style.backgroundImage = "url(" + currentSrc + ")";
    }
  }
  if (shadowCards) {
    for (var i = 0; i < shadowCards.length; i++) {
      var div = document.createElement("DIV");
      shadowCards[i].parentElement.appendChild(div);
      div.classList.add("colored-shadow");

      var currentSrc = shadowCards[i].children[0].getAttribute("src");
      var el = shadowCards[i].nextElementSibling;

      el.style.backgroundImage = "url(" + currentSrc + ")";
    }
  }
}

if (document.querySelector(".blur-shadow-avatar")) {
  var shadowCards = document.querySelectorAll(".blur-shadow-avatar");
  var shadowCardsRounded = document.querySelectorAll(
    ".blur-shadow-avatar.rounded-circle"
  );

  if (shadowCardsRounded) {
    for (var i = 0; i < shadowCardsRounded.length; i++) {
      var div = document.createElement("DIV");
      shadowCardsRounded[i].parentElement.appendChild(div);
      div.classList.add(
        "colored-shadow",
        "rounded",
        "start-0",
        "end-0",
        "mx-auto"
      );

      var avatarClasses = [
        "avatar-xs",
        "avatar-sm",
        "avatar-lg",
        "avatar-xl",
        "avatar-xxl"
      ];

      for (var k = 0; k < avatarClasses.length; k++) {
        if (
          shadowCardsRounded[i].firstElementChild.classList.contains(
            avatarClasses[k]
          )
        ) {
          div.classList.add(avatarClasses[k]);
        }
      }

      var currentSrc = shadowCardsRounded[i].children[0].getAttribute("src");
      var el = shadowCardsRounded[i].nextElementSibling;

      el.style.backgroundImage = "url(" + currentSrc + ")";
    }
  }
  if (shadowCards) {
    for (var i = 0; i < shadowCards.length; i++) {
      var div = document.createElement("DIV");
      shadowCards[i].parentElement.appendChild(div);
      div.classList.add("colored-shadow", "start-0", "end-0", "mx-auto");

      var avatarClasses = [
        "avatar-xs",
        "avatar-sm",
        "avatar-lg",
        "avatar-xl",
        "avatar-xxl"
      ];

      for (var k = 0; k < avatarClasses.length; k++) {
        if (
          shadowCards[i].firstElementChild.classList.contains(avatarClasses[k])
        ) {
          div.classList.add(avatarClasses[k]);
        }
      }

      var currentSrc = shadowCards[i].children[0].getAttribute("src");
      var el = shadowCards[i].nextElementSibling;

      el.style.backgroundImage = "url(" + currentSrc + ")";
    }
  }
}

if (document.querySelector("#google-maps")) {
  var myLatlng = new google.maps.LatLng(40.748817, -73.985428);
  var mapOptions = {
    zoom: 13,
    center: myLatlng,
    scrollwheel: false, 
    styles: [
      {
        featureType: "administrative",
        elementType: "labels.text.fill",
        stylers: [
          {
            color: "#444444"
          }
        ]
      },
      {
        featureType: "landscape",
        elementType: "all",
        stylers: [
          {
            color: "#f2f2f2"
          }
        ]
      },
      {
        featureType: "poi",
        elementType: "all",
        stylers: [
          {
            visibility: "off"
          }
        ]
      },
      {
        featureType: "road",
        elementType: "all",
        stylers: [
          {
            saturation: -100
          },
          {
            lightness: 45
          }
        ]
      },
      {
        featureType: "road.highway",
        elementType: "all",
        stylers: [
          {
            visibility: "simplified"
          }
        ]
      },
      {
        featureType: "road.arterial",
        elementType: "labels.icon",
        stylers: [
          {
            visibility: "off"
          }
        ]
      },
      {
        featureType: "transit",
        elementType: "all",
        stylers: [
          {
            visibility: "off"
          }
        ]
      },
      {
        featureType: "water",
        elementType: "all",
        stylers: [
          {
            color: "#C5CBF5"
          },
          {
            visibility: "on"
          }
        ]
      }
    ]
  };

  var map = new google.maps.Map(
    document.getElementById("google-maps"),
    mapOptions
  );

  var marker = new google.maps.Marker({
    position: myLatlng,
    title: "Hello World!"
  });

  marker.setMap(map);
}

var total = document.querySelectorAll(".nav-pills");

total.forEach(function (item, i) {
  var moving_div = document.createElement("div");
  var first_li = item.querySelector("li:first-child .nav-link");
  var tab = first_li.cloneNode();
  tab.innerHTML = "-";

  moving_div.classList.add("moving-tab", "position-absolute", "nav-link");
  moving_div.appendChild(tab);
  item.appendChild(moving_div);

  var list_length = item.getElementsByTagName("li").length;

  moving_div.style.padding = "0px";
  moving_div.style.width =
    item.querySelector("li:nth-child(1)").offsetWidth + "px";
  moving_div.style.transform = "translate3d(0px, 0px, 0px)";
  moving_div.style.transition = ".5s ease";

  item.onmouseover = function (event) {
    let target = getEventTarget(event);
    let li = target.closest("li"); 
    if (li) {
      let nodes = Array.from(li.closest("ul").children); 
      let index = nodes.indexOf(li) + 1;
      item.querySelector("li:nth-child(" + index + ") .nav-link").onclick =
        function () {
          moving_div = item.querySelector(".moving-tab");
          let sum = 0;
          if (item.classList.contains("flex-column")) {
            for (var j = 1; j <= nodes.indexOf(li); j++) {
              sum += item.querySelector("li:nth-child(" + j + ")").offsetHeight;
            }
            moving_div.style.transform = "translate3d(0px," + sum + "px, 0px)";
            moving_div.style.height = item.querySelector(
              "li:nth-child(" + j + ")"
            ).offsetHeight;
          } else {
            for (var j = 1; j <= nodes.indexOf(li); j++) {
              sum += item.querySelector("li:nth-child(" + j + ")").offsetWidth;
            }
            moving_div.style.transform = "translate3d(" + sum + "px, 0px, 0px)";
            moving_div.style.width =
              item.querySelector("li:nth-child(" + index + ")").offsetWidth +
              "px";
          }
        };
    }
  };
});

window.addEventListener("resize", function (event) {
  total.forEach(function (item, i) {
    item.querySelector(".moving-tab").remove();
    var moving_div = document.createElement("div");
    var tab = item.querySelector(".nav-link.active").cloneNode();
    tab.innerHTML = "-";

    moving_div.classList.add("moving-tab", "position-absolute", "nav-link");
    moving_div.appendChild(tab);

    item.appendChild(moving_div);

    moving_div.style.padding = "0px";
    moving_div.style.transition = ".5s ease";

    let li = item.querySelector(".nav-link.active").parentElement;

    if (li) {
      let nodes = Array.from(li.closest("ul").children); 
      let index = nodes.indexOf(li) + 1;

      let sum = 0;
      if (item.classList.contains("flex-column")) {
        for (var j = 1; j <= nodes.indexOf(li); j++) {
          sum += item.querySelector("li:nth-child(" + j + ")").offsetHeight;
        }
        moving_div.style.transform = "translate3d(0px," + sum + "px, 0px)";
        moving_div.style.width =
          item.querySelector("li:nth-child(" + index + ")").offsetWidth + "px";
        moving_div.style.height = item.querySelector(
          "li:nth-child(" + j + ")"
        ).offsetHeight;
      } else {
        for (var j = 1; j <= nodes.indexOf(li); j++) {
          sum += item.querySelector("li:nth-child(" + j + ")").offsetWidth;
        }
        moving_div.style.transform = "translate3d(" + sum + "px, 0px, 0px)";
        moving_div.style.width =
          item.querySelector("li:nth-child(" + index + ")").offsetWidth + "px";
      }
    }
  });

  if (window.innerWidth < 991) {
    total.forEach(function (item, i) {
      if (!item.classList.contains("flex-column")) {
        item.classList.add("flex-column", "on-resize");
      }
    });
  } else {
    total.forEach(function (item, i) {
      if (item.classList.contains("on-resize")) {
        item.classList.remove("flex-column", "on-resize");
      }
    });
  }
});

function getEventTarget(e) {
  e = e || window.event;
  return e.target || e.srcElement;
}

function copyCode(el) {
  const selection = window.getSelection();
  const range = document.createRange();
  const textToCopy = el.nextElementSibling;
  range.selectNodeContents(textToCopy);
  selection.removeAllRanges();
  selection.addRange(range);
  const successful = document.execCommand("copy");
  window.getSelection().removeAllRanges();
  if (!el.parentElement.querySelector(".alert")) {
    var alert = document.createElement("div");
    alert.classList.add(
      "alert",
      "alert-success",
      "position-absolute",
      "top-0",
      "border-0",
      "text-white",
      "w-25",
      "end-0",
      "start-0",
      "mt-2",
      "mx-auto",
      "py-2"
    );
    alert.style.transform = "translate3d(0px, 0px, 0px)";
    alert.style.opacity = "0";
    alert.style.transition = ".35s ease";
    setTimeout(function () {
      alert.style.transform = "translate3d(0px, 20px, 0px)";
      alert.style.setProperty("opacity", "1", "important");
    }, 100);
    alert.innerHTML = "Code successfully copied!";
    el.parentElement.appendChild(alert);
    setTimeout(function () {
      alert.style.transform = "translate3d(0px, 0px, 0px)";
      alert.style.setProperty("opacity", "0", "important");
    }, 2000);
    setTimeout(function () {
      el.parentElement.querySelector(".alert").remove();
    }, 2500);
  }
}
