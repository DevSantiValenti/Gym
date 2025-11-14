// Ejemplo de interacción simple
document.getElementById("busqueda").addEventListener("keyup", function(e) {
  if (e.key === "Enter") {
    alert("Buscando socio: " + e.target.value);
  }
});