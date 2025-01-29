{{/*
common labels
*/}}
{{- define "common.labels" }}
app: {{ .Values.application.name }}
{{- end }}

{{/*
monitoring labels
*/}}
{{- define "monitoring.labels" -}}
{{ if .Values.monitoring.enabled }}
{{ .Values.monitoring.monitoringLabelName }}: {{ .Values.monitoring.monitoringLabelValue }}
{{- end }}
{{- end }}


