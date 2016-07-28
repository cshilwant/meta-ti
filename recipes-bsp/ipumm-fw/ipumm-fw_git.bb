python __anonymous() {
    features = bb.data.getVar("MACHINE_FEATURES", d, 1)
    if not features:
        return
    if "mmip" not in features:
        raise bb.parse.SkipPackage('ipumm-fw does not apply to systems without the "mmip" flag in MACHINE_FEATURES')
}

DESCRIPTION = "Firmware for IPU for supporting Accelerated MM decode and encode"
LICENSE = "TI-TSPA"

LIC_FILES_CHKSUM = "file://Texas_Instruments_ipumm_Manifest.pdf;md5=e3e7d6efc7aca87789775198c4ca9f8e"

COMPATIBLE_MACHINE = "dra7xx"

RDEPENDS_${PN} = " libdce"

SRC_URI = "git://git.ti.com/ivimm/ipumm.git;protocol=git"

SRCREV = "7c283ebc252223a728cb542dbc10c3ca0ab53e59"

S = "${WORKDIR}/git"

PV = "3.00.11.00"
PR = "r4"

require recipes-ti/includes/ti-paths.inc
require recipes-ti/includes/ti-staging.inc

inherit update-alternatives

DEPENDS = "ti-xdctools ti-sysbios ti-codec-engine ti-framework-components ti-xdais ti-ccsv6-native ti-ipc-rtos"

export HWVERSION="ES10"
export BIOSTOOLSROOT="${STAGING_DIR_TARGET}/usr/share/ti"

export XDCVERSION="ti-xdctools-tree"
export BIOSVERSION="ti-sysbios-tree"
export IPCVERSION="ti-ipc-tree"
export CEVERSION="ti-codec-engine-tree"
export FCVERSION="ti-framework-components-tree"
export XDAISVERSION="ti-xdais-tree"

export TMS470CGTOOLPATH="${M4_TOOLCHAIN_INSTALL_DIR}"
export IPCSRC="${STAGING_DIR_TARGET}/usr/share/ti/ti-ipc-tree"

do_configure() {
    oe_runmake unconfig
    oe_runmake vayu_smp_config
}

do_compile() {
    oe_runmake
}

TARGET = "dra7-ipu2-fw.xem4"
TARGET_MAP = "platform/ti/dce/baseimage/package/cfg/out/ipu/release/ipu.xem4.map"

do_install() {
    install -d ${D}${base_libdir}/firmware
    install -m 0644 ${S}/${TARGET} ${D}${base_libdir}/firmware/${TARGET}.${BPN}
    install -m 0644 ${S}/${TARGET_MAP} ${D}${base_libdir}/firmware/${TARGET}.map
}

ALTERNATIVE_${PN} = "dra7-ipu2-fw.xem4"
ALTERNATIVE_LINK_NAME[dra7-ipu2-fw.xem4] = "${base_libdir}/firmware/${TARGET}"
ALTERNATIVE_TARGET[dra7-ipu2-fw.xem4] = "${base_libdir}/firmware/${TARGET}.${BPN}"
ALTERNATIVE_PRIORITY = "20"

FILES_${PN} += "${base_libdir}/firmware/*"

PR = "r2"
