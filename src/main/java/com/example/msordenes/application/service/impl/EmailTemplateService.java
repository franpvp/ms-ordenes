package com.example.msordenes.application.service.impl;

import com.example.msordenes.application.dto.DetalleOrdenDto;
import com.example.msordenes.application.dto.OrdenDto;
import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

    public String generarTemplateCompra(OrdenDto orden, String nombreCliente) {

        StringBuilder listaProductos = new StringBuilder();

        for (DetalleOrdenDto det : orden.getListaDetalle()) {
            listaProductos.append("""
                <tr style="border-bottom:1px solid #e5e7eb;">
                    <td style="padding:12px; display:flex; align-items:center;">
                        <img src="%s" width="60" height="60" style="border-radius:8px; margin-right:12px;">
                        <div>
                            <div style="font-weight:600; font-size:14px;">%s</div>
                            <div style="font-size:12px; color:#6b7280;">Cantidad: %s</div>
                        </div>
                    </td>
                    <td style="padding:12px; text-align:right; font-weight:600;">
                        $%s
                    </td>
                </tr>
            """.formatted(
                    det.getProducto().getImagenUrl(),
                    det.getProducto().getNombre(),
                    det.getCantidad(),
                    det.getSubtotal()
            ));
        }

        int total = orden.getTotalPrecio() + 4990;

        return """
        <html>
        <body style="font-family:'Segoe UI',Arial,sans-serif; background:#f3f4f6; padding:20px;">
        <div style="max-width:650px; margin:auto; background:white; border-radius:12px; padding:30px;">

            <h2 style="text-align:center; color:#111827;">Gracias por tu compra</h2>
            <p style="text-align:center; color:#6b7280;">
                Hola <strong>%s</strong>, tu pedido se ha procesado correctamente.
            </p>

            <h3 style="margin-top:25px; margin-bottom:10px; color:#111827;">Orden Nº <span style="color:#ef4444;">%s</span></h3>

            <table style="width:100%%; border-collapse:collapse; margin-top:15px;">
                %s
            </table>

            <div style="margin-top:25px; font-size:15px;">
                <p>Subtotal: <strong>$%s</strong></p>
                <p>Costo de envío: <strong>$4.990</strong></p>
                <p style="font-size:18px; font-weight:bold; margin-top:10px;">
                    Total pagado: <span style="color:#ef4444;">$%s</span>
                </p>
            </div>

            <p style="margin-top:35px; color:#6b7280; text-align:center; font-size:13px;">
                Puedes revisar el estado de tu pedido en tu cuenta de TechFactory.
            </p>

        </div>
        </body>
        </html>
        """.formatted(
                nombreCliente,
                orden.getIdOrden(),
                listaProductos,
                orden.getTotalPrecio(),
                total
        );
    }
}