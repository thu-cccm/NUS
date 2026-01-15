function focused(el) {
  if (el.parentElement.classList.contains("input-group")) {
    el.parentElement.classList.add("focused");
  }
}

function defocused(el) {
  if (el.parentElement.classList.contains("input-group")) {
    el.parentElement.classList.remove("focused");
  }
}

function setAttributes(el, options) {
  Object.keys(options).forEach(function (attr) {
    el.setAttribute(attr, options[attr]);
  });
}

if (document.querySelectorAll(".input-group").length != 0) {
  var allInputs = document.querySelectorAll("input.form-control");
  allInputs.forEach((el) =>
    setAttributes(el, {
      onfocus: "focused(this)",
      onfocusout: "defocused(this)"
    })
  );
}

export default function setMaterialInput() {

  var inputs = document.querySelectorAll("input");

  for (var i = 0; i < inputs.length; i++) {
    if (inputs[i].hasAttribute("value")) {
      inputs[i].parentElement.classList.add("is-filled");
    }
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
}
