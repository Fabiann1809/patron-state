const API_URL = "/api/flight";

const STATE_LABELS_ES = {
    GROUND: "En tierra",
    PREFLIGHT_CHECKS: "Comprobaciones previas",
    READY_TO_ARM: "Listo para armar",
    ARMED: "Armado",
    FLYING: "En vuelo",
    RTL: "Regreso automático",
    EMERGENCY: "Emergencia",
};

const EVENT_LABELS_ES = {
    START_CHECKS: "Iniciar comprobaciones",
    CHECKS_PASSED: "Comprobaciones correctas",
    CHECKS_FAILED: "Comprobaciones fallidas",
    ARM: "Armar",
    DISARM: "Desarmar",
    TAKEOFF: "Despegar",
    REQUEST_RTL: "Regresar al inicio",
    LAND_COMPLETE: "Aterrizaje completado",
    EMERGENCY_TRIGGER: "Emergencia",
};

const MESSAGE_LABELS_ES = {
    CHECKS_STARTED: "Comprobaciones iniciadas.",
    CHECKS_OK: "Comprobaciones superadas. Sistema listo para armar.",
    ARMED: "Sistema armado.",
    RETURNED_TO_GROUND: "Vuelto a tierra tras fallo de comprobaciones.",
    PREFLIGHT_ABORTED: "Comprobaciones abortadas. En tierra.",
    DISARMED: "Desarmado.",
    TAKEOFF_COMPLETE: "Despegue completado.",
    EMERGENCY_ACTIVE: "Emergencia activada.",
    RTL_ENGAGED: "Regreso automático activado.",
    LANDED_DISARMED: "Aterrizaje completado. En tierra.",
    RTL_LANDED_DISARMED: "Regreso finalizado. Sistema en tierra.",
    EMERGENCY_RESOLVED_ON_GROUND: "Emergencia resuelta. En tierra.",
    INVALID_TRANSITION: "Acción no permitida en este modo.",
};

const STATE_HINTS_ES = {
    GROUND: "Motores sin autorización de vuelo. Inicia comprobaciones para continuar.",
    PREFLIGHT_CHECKS: "Verificando sensores, batería y sistemas de seguridad.",
    READY_TO_ARM: "Comprobaciones superadas. Arma el sistema para continuar o aborta si es necesario.",
    ARMED: "Listo para despegar. Puedes desarmar o declarar emergencia.",
    FLYING: "En misión o vuelo manual. Puedes regresar al inicio, aterrizar o declarar emergencia.",
    RTL: "Regresando automáticamente al punto de inicio. Completa el aterrizaje cuando corresponda.",
    EMERGENCY: "Prioridad máxima. Completa el aterrizaje forzado y confirma en tierra.",
};

const els = {
    stateLabel: document.getElementById("stateLabel"),
    stateId: document.getElementById("stateId"),
    stateHint: document.getElementById("stateHint"),
    stateRing: document.getElementById("stateRing"),
    actions: document.getElementById("actions"),
    actionCount: document.getElementById("actionCount"),
    log: document.getElementById("log"),
    refreshBtn: document.getElementById("refreshBtn"),
    clearLogBtn: document.getElementById("clearLogBtn"),
};

function eventButtonClass(eventName) {
    if (eventName === "EMERGENCY_TRIGGER") return "btn danger";
    if (eventName === "CHECKS_FAILED") return "btn secondary";
    return "btn primary";
}

function setStateVisual(stateId) {
    els.stateRing.classList.remove("emergency", "rtl", "flying");
    if (stateId === "EMERGENCY") els.stateRing.classList.add("emergency");
    else if (stateId === "RTL") els.stateRing.classList.add("rtl");
    else if (stateId === "FLYING") els.stateRing.classList.add("flying");
}

function appendLog(text, ok) {
    const li = document.createElement("li");
    const time = document.createElement("time");
    time.dateTime = new Date().toISOString();
    time.textContent = new Date().toLocaleTimeString("es", { hour: "2-digit", minute: "2-digit", second: "2-digit" });
    const span = document.createElement("span");
    span.textContent = text;
    span.className = ok ? "ok" : "err";
    li.appendChild(time);
    li.appendChild(span);
    els.log.prepend(li);
}

async function refreshStatus() {
    const res = await fetch(API_URL);
    if (!res.ok) throw new Error("STATUS_FETCH_FAILED");
    const data = await res.json();
    renderStatus(data);
    return data;
}

function renderStatus(data) {
    const sid = data.currentState;
    els.stateId.textContent = sid;
    els.stateLabel.textContent = STATE_LABELS_ES[sid] || sid;
    els.stateHint.textContent = STATE_HINTS_ES[sid] || "";
    setStateVisual(sid);

    const events = data.allowedEvents || [];
    els.actionCount.textContent = String(events.length);
    els.actions.innerHTML = "";

    events.forEach((eventName) => {
        const btn = document.createElement("button");
        btn.type = "button";
        btn.className = eventButtonClass(eventName);
        btn.textContent = EVENT_LABELS_ES[eventName] || eventName;
        btn.addEventListener("click", () => sendEvent(eventName));
        els.actions.appendChild(btn);
    });
}

async function sendEvent(eventName) {
    const buttons = [...els.actions.querySelectorAll("button")];
    buttons.forEach((b) => (b.disabled = true));
    try {
        const res = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ event: eventName }),
        });
        const data = await res.json();
        renderStatus(data);
        const ok = data.lastSuccess === true;
        const msg =
            MESSAGE_LABELS_ES[data.lastMessageCode] || data.lastMessageCode || (ok ? "Hecho." : "Error.");
        appendLog(msg, ok);
    } catch (e) {
        appendLog("No se pudo contactar al servidor.", false);
    } finally {
        [...els.actions.querySelectorAll("button")].forEach((b) => (b.disabled = false));
    }
}

els.refreshBtn.addEventListener("click", () => {
    refreshStatus().catch(() => appendLog("No se pudo actualizar el estado.", false));
});

els.clearLogBtn.addEventListener("click", () => {
    els.log.innerHTML = "";
});

refreshStatus().catch(() => {
    els.stateHint.textContent = "No se pudo cargar el estado. Verifica que el servidor esté en ejecución.";
});
