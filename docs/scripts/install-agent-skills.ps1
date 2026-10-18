#Requires -Version 5.1

<#
.SYNOPSIS
    将 agent skills 安装或更新到当前项目的 .agents/skills，并为 Qoder、TRAE、CodeBuddy
    创建项目级 skills 目录的目录联结。

.DESCRIPTION
    1. 通过 npx skills add 安装 skills 到 .agents/skills。
    2. 通过 npx skills update 更新已安装的 skills。
    3. 若 .qoder/skills、.trae/skills、.codebuddy/skills 不存在，则创建目录联结指向 .agents/skills。
    4. 若这些目录已经存在且不是联结，则不会覆盖，避免误删已有内容。

.NOTES
    需要安装 Node.js，并确保 npx 可用。
    创建目录联结需要管理员权限的 PowerShell / 终端。
#>

# 安装某个仓库的所有 skills，并创建 IDE 目录联结
# .\install-agent-skills.ps1 -Source vercel-labs/agent-skills -All

# 只安装指定 skill
# .\install-agent-skills.ps1 -Source vercel-labs/agent-skills -Skill frontend-design

# 不安装新 skill，只补建 Qoder / TRAE / CodeBuddy 的目录联结
# .\install-agent-skills.ps1 -LinkOnly

# 更新 .agents/skills 中已有的 skills
# .\install-agent-skills.ps1 -Update

# 更新指定仓库的 skills
# .\install-agent-skills.ps1 -Update -Source vercel-labs/agent-skills

param(
    # GitHub 仓库，例如 vercel-labs/agent-skills
    [Parameter(Mandatory = $false)]
    [string]$Source,

    # 指定要安装的单个 skill 名称
    [Parameter(Mandatory = $false)]
    [string]$Skill,

    # 安装仓库中的所有 skills
    [switch]$All,

    # 跳过 npx 安装步骤，仅创建目录联结
    [switch]$LinkOnly,

    # 更新已安装的 skills
    [switch]$Update
)

$ErrorActionPreference = "Stop"

function Test-Admin {
    $principal = New-Object Security.Principal.WindowsPrincipal(
        [Security.Principal.WindowsIdentity]::GetCurrent()
    )
    return $principal.IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
}

function Ensure-DirectoryJunction {
    param(
        [string]$LinkPath,
        [string]$TargetPath
    )

    $linkDir = Resolve-Path $LinkPath -ErrorAction SilentlyContinue

    if ($linkDir) {
        $item = Get-Item $linkDir
        if ($item.Attributes -match "ReparsePoint") {
            Write-Host "已存在目录联结: $LinkPath -> $($item.Target)" -ForegroundColor Green
        }
        else {
            Write-Warning "路径 $LinkPath 已存在且不是目录联结，跳过创建。"
        }
        return
    }

    $parent = Split-Path $LinkPath -Parent
    if (-not (Test-Path $parent)) {
        New-Item -ItemType Directory -Path $parent -Force | Out-Null
    }

    $absoluteTarget = (Resolve-Path $TargetPath).ProviderPath
    $absoluteLink = (New-Object System.IO.DirectoryInfo $LinkPath).FullName

    Write-Host "创建目录联结: $absoluteLink -> $absoluteTarget" -ForegroundColor Cyan
    cmd /c "mklink /J `"$absoluteLink`" `"$absoluteTarget`"" | Out-Null

    if (Test-Path $LinkPath) {
        Write-Host "目录联结创建成功: $LinkPath" -ForegroundColor Green
    }
    else {
        throw "创建目录联结失败: $LinkPath"
    }
}

$projectRoot = Get-Location
$agentsSkills = Join-Path $projectRoot ".agents\skills"

$ideDirs = @(
    @{ Name = "Qoder";  Path = Join-Path $projectRoot ".qoder\skills" }
    @{ Name = "TRAE";   Path = Join-Path $projectRoot ".trae\skills" }
    @{ Name = "CodeBuddy"; Path = Join-Path $projectRoot ".codebuddy\skills" }
)

if ($Update) {
    if (-not (Get-Command npx -ErrorAction SilentlyContinue)) {
        throw "未找到 npx，请先安装 Node.js。"
    }

    $npxArgs = @("skills", "update")

    if ($Source) {
        $npxArgs += $Source
    }

    if ($All) {
        $npxArgs += "--all"
    }

    if ($Skill) {
        $npxArgs += "--skill", $Skill
    }

    Write-Host "执行: npx $($npxArgs -join ' ')" -ForegroundColor Cyan
    & npx $npxArgs

    if ($LASTEXITCODE -ne 0) {
        throw "npx skills update 失败，退出码: $LASTEXITCODE"
    }
}
elseif (-not $LinkOnly) {
    if (-not $Source -and -not $All) {
        Write-Host "未指定 --Source 或 --All，将仅创建 IDE 目录联结。" -ForegroundColor Yellow
    }
    else {
        if (-not (Get-Command npx -ErrorAction SilentlyContinue)) {
            throw "未找到 npx，请先安装 Node.js。"
        }

        if (-not (Test-Path $agentsSkills)) {
            New-Item -ItemType Directory -Path $agentsSkills -Force | Out-Null
        }

        $npxArgs = @("skills", "add")

        if ($Source) {
            $npxArgs += $Source
        }

        if ($All) {
            $npxArgs += "--skill", "*"
        }

        if ($Skill) {
            $npxArgs += "--skill", $Skill
        }

        $npxArgs += "-y"

        Write-Host "执行: npx $($npxArgs -join ' ')" -ForegroundColor Cyan
        & npx $npxArgs

        if ($LASTEXITCODE -ne 0) {
            throw "npx skills 安装失败，退出码: $LASTEXITCODE"
        }
    }
}

if (-not (Test-Path $agentsSkills)) {
    Write-Warning "未找到 $agentsSkills，将不会创建 IDE 目录联结。"
    exit 0
}

if (-not (Test-Admin)) {
    Write-Warning "当前终端没有管理员权限，可能无法创建目录联结。"
    Write-Warning "请以管理员身份运行 PowerShell 后重新执行。"
}

foreach ($ide in $ideDirs) {
    Ensure-DirectoryJunction -LinkPath $ide.Path -TargetPath $agentsSkills
}

Write-Host "完成。" -ForegroundColor Green