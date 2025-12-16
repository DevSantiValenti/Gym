document.getElementById('busqueda').addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        const dni = this.value.trim();
        // console.log("Buscando DNI:", dni);
        if (dni === '') return;

        fetch(`/api/socios/dni?dni=${encodeURIComponent(dni)}`)
            .then(response => {
                // console.log("Respuesta cruda:", response);
                if (!response.ok) throw new Error('Socio no encontrado');
                return response.json();
            })
            .then(socio => {

                // console.log("Socio recibido:", socio);  // ← AGREGAR ESTE
                // console.log("Actividad recibida:", socio.actividad); // ← Y ESTE

                const parseLocalDate = (fechaStr) => {
                    if (!fechaStr) return null;
                    const [year, month, day] = fechaStr.split('-').map(Number);
                    return new Date(year, month - 1, day, 12, 0, 0); // mes en JS es 0-based
                };

                const fechaVto = parseLocalDate(socio.fechaVencimiento);

                let diasRestantes = null;

                if (fechaVto) {
                    const hoy = new Date();
                    const hoyFlat = new Date(hoy.getFullYear(), hoy.getMonth(), hoy.getDate(), 12, 0, 0);

                    // NO reconstruyas fechaVto, ya es una fecha "plana"
                    diasRestantes = Math.floor((fechaVto - hoyFlat) / (1000 * 60 * 60 * 24));
                }

                const ultIngreso = socio.ultIngreso ? new Date(socio.ultIngreso) : null;

                // Mostrar datos
                document.getElementById('datos-socio').innerHTML = `
            <p><strong>Nombre:</strong> ${socio.nombreCompleto}</p>
            <p><strong>Actividad:</strong> ${socio.actividad ? socio.actividad.nombre : 'N/A'}</p>
            <p><strong>Frecuencia:</strong> ${socio.vecesIngresado || 0} veces ingresado</p>
            <p><strong>Vencimiento:</strong> ${fechaVto ? fechaVto.toLocaleDateString('es-AR') : 'N/A'}</p>
          `;

                document.getElementById('tarjeta').style.display = 'flex';

                // --- ESTADO CUOTA ---
                const estadoDiv = document.getElementById('estado');
                estadoDiv.style.display = 'block';

                const tarjeta = document.getElementById('tarjeta');

                // BORRO botón anterior si existe
                const btnPrevio = document.getElementById('boton-abonar');
                if (btnPrevio) btnPrevio.remove();

                // Prioridad: vencida -> próximo (≤5 días) -> al día
                if (socio.cuotaPaga === false || (diasRestantes !== null && diasRestantes <= 0)) {
                    // 0 días → HOY es el vencimiento → se considera vencida
                    estadoDiv.innerHTML = '<h3>CUOTA VENCIDA</h3>';
                    estadoDiv.className = 'estado cuota-vencida';

                    // *** AGREGAR BOTÓN ***
                    const btn = document.createElement('a');
                    btn.id = 'boton-abonar';
                    btn.href = `/socios/abonarCuota/${socio.id}`;
                    btn.textContent = "ABONAR CUOTA";
                    btn.className = "btn-abonar";

                    estadoDiv.appendChild(btn);

                } else if (diasRestantes !== null && diasRestantes <= 5) {
                    // Entre 1 y 5 días → advertencia
                    estadoDiv.innerHTML =
                        `<h3>LA CUOTA VENCE EN ${diasRestantes} DÍA${diasRestantes === 1 ? '' : 'S'}</h3>`;
                    estadoDiv.className = 'estado cuota-proximo';

                } else {
                    estadoDiv.innerHTML = '<h3>CUOTA AL DÍA</h3>';
                    estadoDiv.className = 'estado cuota-al-dia';
                }

                // console.log("Fecha recibida del backend:", socio.fechaVencimiento);

                document.getElementById('alertas').innerHTML = '';
            })
            .catch(() => {
                document.getElementById('alertas').innerHTML = '<div class="error">❌ Socio no encontrado</div>';
                document.getElementById('tarjeta').style.display = 'none';
                document.getElementById('estado').style.display = 'none';
            });
    }
});