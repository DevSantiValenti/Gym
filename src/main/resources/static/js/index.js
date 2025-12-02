document.getElementById('busqueda').addEventListener('keypress', function (e) {
    if (e.key === 'Enter') {
        const dni = this.value.trim();
        if (dni === '') return;

        fetch(`/api/socios/dni?dni=${encodeURIComponent(dni)}`)
            .then(response => {
                if (!response.ok) throw new Error('Socio no encontrado');
                return response.json();
            })
            .then(socio => {

                // --- FIX FECHAS ---
                // Convertimos LocalDate (YYYY-MM-DD) a fecha estable sin problemas de timezone
                const parseLocalDate = (fecha) =>
                    fecha ? new Date(`${fecha}T12:00:00`) : null;

                const fechaVto = parseLocalDate(socio.fechaVencimiento);
                const ultIngreso = socio.ultIngreso ? new Date(socio.ultIngreso) : null;

                // Mostrar datos
                document.getElementById('datos-socio').innerHTML = `
            <p><strong>Nombre:</strong> ${socio.nombreCompleto}</p>
            <p><strong>Actividad:</strong> ${socio.actividad || 'N/A'}</p>
            <p><strong>Frecuencia:</strong> ${socio.vecesIngresado || 0} veces ingresado</p>
            <p><strong>Vencimiento:</strong> ${fechaVto ? fechaVto.toLocaleDateString('es-AR') : 'N/A'}</p>
            <p><strong>Ultimo Ingreso:</strong> ${ultIngreso
                        ? ultIngreso.toLocaleString('es-AR', {
                            dateStyle: 'short',
                            timeStyle: 'short',
                            hourCycle: 'h23'
                        })
                        : 'N/A'}
            </p>
          `;

                document.getElementById('tarjeta').style.display = 'flex';


                // --- ESTADO CUOTA ---
                const estadoDiv = document.getElementById('estado');
                estadoDiv.style.display = 'block';

                let diasRestantes = null;

                if (fechaVto) {
                    const hoy = new Date();

                    const utcHoy = Date.UTC(hoy.getFullYear(), hoy.getMonth(), hoy.getDate());
                    const utcVto = Date.UTC(fechaVto.getFullYear(), fechaVto.getMonth(), fechaVto.getDate());

                    diasRestantes = Math.floor((utcVto - utcHoy) / (1000 * 60 * 60 * 24));
                }

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
                    btn.href = `/socios/editar/${socio.id}`;
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



                document.getElementById('alertas').innerHTML = '';
            })
            .catch(() => {
                document.getElementById('alertas').innerHTML = '<div class="error">❌ Socio no encontrado</div>';
                document.getElementById('tarjeta').style.display = 'none';
                document.getElementById('estado').style.display = 'none';
            });
    }
});