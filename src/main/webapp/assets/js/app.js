document.addEventListener("DOMContentLoaded", () => {
    const layoutShell = document.querySelector(".layout-shell");
    const sidebarToggle = document.querySelector(".sidebar-toggle");

    if (sidebarToggle && layoutShell) {
        sidebarToggle.addEventListener("click", () => {
            layoutShell.classList.toggle("sidebar-open");
        });
    }

    document.querySelectorAll(".nav-group-toggle").forEach((toggle) => {
        toggle.addEventListener("click", () => {
            toggle.parentElement.classList.toggle("is-open");
        });
    });

    document.querySelectorAll(".flash-close").forEach((button) => {
        button.addEventListener("click", () => {
            const flash = button.closest(".flash");
            if (flash) {
                flash.remove();
            }
        });
    });

    const modal = document.querySelector("[data-call-modal]");
    if (modal) {
        const modalTitle = modal.querySelector("[data-call-title]");
        const modalText = modal.querySelector("[data-call-text]");
        const closeButtons = modal.querySelectorAll("[data-call-close]");

        document.querySelectorAll(".js-call-trigger").forEach((button) => {
            button.addEventListener("click", () => {
                const type = button.dataset.type;
                const target = button.dataset.target;
                modalTitle.textContent = type === "video" ? "Chamada de Video" : "Chamada de Voz";
                modalText.textContent = "Conectando com " + target + " em ambiente simulado.";
                modal.classList.add("is-open");
            });
        });

        closeButtons.forEach((button) => {
            button.addEventListener("click", () => modal.classList.remove("is-open"));
        });

        modal.addEventListener("click", (event) => {
            if (event.target === modal) {
                modal.classList.remove("is-open");
            }
        });
    }
});
