package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;

public class Form {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName("Grupo Solidario")
    private String grupoSolidario;

    @ExcelCellName("Asesora")
    private String asesora;

    @ExcelCellName("Nombre_Beneficiario")
    private String nombreBeneficiario;

    @ExcelCellName("Apellido_Beneficiario")
    private String apellidoBeneficiario;

    @ExcelCellName("Fecha_Nacimiento_Beneficiario")
    private String fechaNacimientoBeneficiario;

    @ExcelCellName("Edad_Beneficiario")
    private String edadBeneficiario;

    @ExcelCellName("Estado_Civil_Beneficiario")
    private String estadoCivilBeneficiario;

    @ExcelCellName("Estado_Civil_Otro_Beneficiario")
    private String estadoCivilOtroBeneficiario;

    //TODO: add missing form columns
}
