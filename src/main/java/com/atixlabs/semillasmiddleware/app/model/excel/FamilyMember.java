package com.atixlabs.semillasmiddleware.app.model.excel;

import com.poiji.annotation.ExcelCellName;
import com.poiji.annotation.ExcelRow;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter

public class FamilyMember {

    @ExcelRow
    private int rowIndex;

    @ExcelCellName("Parentesco_Familiar")
    private String parentescoFamiliar;

    @ExcelCellName("Parentesco_Otro_Familiar")
    private String parentescoOtroFamiliar;

    @ExcelCellName("Apellido_Familiar")
    private String apellidoFamiliar;

    @ExcelCellName("Nombre_Familiar")
    private String nombreFamiliar;

    @ExcelCellName("Fecha_Nacimiento_Familiar")
    private String fechaNacimientoFamiliar;

    @ExcelCellName("Edad_Familiar")
    private String edadFamiliar;

    @ExcelCellName("Genero_Familia")
    private String generoFamilia;

    @ExcelCellName("Genero_Otro_Familiar")
    private String generoOtroFamiliar;

    @ExcelCellName("Tipo_Documento_Familiar")
    private String tipoDocumentoFamiliar;

    @ExcelCellName("Numero_Doc_Familiar")
    private Long numeroDniFamiliar;

    @ExcelCellName("_parent_index")
    private int parentIndex;

}
