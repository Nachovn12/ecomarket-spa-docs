# build-all.ps1
# Compila los 8 proyectos Spring Boot del repo ecomarket-spa
# Uso: .\build-all.ps1            -> solo compila
#      .\build-all.ps1 -Test      -> compila + corre tests
#      .\build-all.ps1 -Clean     -> clean install

param(
  [switch]$Test,
  [switch]$Clean
)

$modules = @(
  "ms-usuarios-identidad",
  "ms-catalogo",
  "ms-inventario-abastecimiento",
  "ms-pedidos-ventas",
  "ms-logistica-envios",
  "ms-administracion-soporte",
  "ms-reportes",
  "api-gateway"
)

$goal = if ($Clean) { "clean install" } else { "install" }
if ($Test) { $skip = "" } else { $skip = "-DskipTests" }

$failed = @()
foreach ($ms in $modules) {
  $pom = "$ms\pom.xml"
  if (-not (Test-Path $pom)) {
    Write-Host "[SKIP] $ms (no tiene pom.xml)" -ForegroundColor Yellow
    continue
  }
  Write-Host "=== $ms ===" -ForegroundColor Cyan
  & mvn -f $pom $goal $skip -q 2>&1 | Tee-Object -FilePath "$env:TEMP\mvn_$ms.log" | Out-Null
  $code = $LASTEXITCODE
  if ($code -eq 0) {
    Write-Host "  [OK] BUILD SUCCESS" -ForegroundColor Green
  } else {
    Write-Host "  [FAIL] exit=$code" -ForegroundColor Red
    $failed += $ms
  }
}

if ($failed.Count -gt 0) {
  Write-Host ""
  Write-Host "Modulos con error:" -ForegroundColor Red
  $failed | ForEach-Object { Write-Host "  - $_" }
  exit 1
} else {
  Write-Host ""
  Write-Host "Todos los modulos compilaron OK." -ForegroundColor Green
  exit 0
}